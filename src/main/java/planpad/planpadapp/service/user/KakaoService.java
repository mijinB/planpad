package planpad.planpadapp.service.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import planpad.planpadapp.dto.user.SocialUserDto;

import java.util.Map;

@Slf4j
@Service
public class KakaoService {

    @Value("${kakao.client_id}")
    private String CLIENT_ID;
    @Value("${kakao.redirect_uri}")
    private String REDIRECT_URI;
    @Value("${kakao.token_url}")
    private String TOKEN_URL;
    @Value("${kakao.info_url}")
    private String INFO_URL;
    @Value("${kakao.logout_url}")
    private String LOGOUT_URL;
    @Value("${kakao.unlink_url}")
    private String UNLINK_URL;

    WebClient webClient = WebClient.create();
    ObjectMapper objectMapper = new ObjectMapper();

    public String kakaoGetAccessToken(String code) {

        try {
            Map<String, String> response = webClient.post()
                    .uri(TOKEN_URL)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .bodyValue("grant_type=authorization_code" +
                            "&client_id=" + CLIENT_ID +
                            "&redirect_uri=" + REDIRECT_URI +
                            "&code=" + code)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                    .block();       // 동기적으로 응답 값을 반환

            if (response == null || !response.containsKey("access_token")) {
                throw new RuntimeException("kakaoGetAccessToken 실패");
            }

            return response.get("access_token");

        } catch (Exception e) {
            throw new RuntimeException("kakaoGetAccessToken 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public SocialUserDto kakaoGetUserInfo(String accessToken) {

        try {
            String response = webClient.get()
                    .uri(INFO_URL)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode rootNode = objectMapper.readTree(response);
            SocialUserDto socialUser = new SocialUserDto();
            socialUser.setSocialId(rootNode.path("id").toString());
            socialUser.setSocialType("kakao");
            socialUser.setAccessToken(accessToken);
            socialUser.setEmail(rootNode.path("kakao_account").path("email").asText());
            socialUser.setName(rootNode.path("properties").path("nickname").asText());
            socialUser.setAvatar(rootNode.path("properties").path("thumbnail_image").asText());

            return socialUser;

        } catch (Exception e) {
            throw new RuntimeException("kakaoGetUserInfo 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public String kakaoUnLink(String accessToken) {

        try {
            Map<String, String> response = webClient.post()
                    .uri(UNLINK_URL)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                    .block();

            if (response == null || !response.containsKey("id")) {
                throw new RuntimeException("kakaoUnLink 실패");
            }

            return response.get("id").toString();

        } catch (Exception e) {
            throw new RuntimeException("kakaoUnLink 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
