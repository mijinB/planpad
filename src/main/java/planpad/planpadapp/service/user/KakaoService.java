package planpad.planpadapp.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import planpad.planpadapp.dto.user.UserDto;

import java.util.Map;

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
    }

    public UserDto kakaoGetUserInfo(String accessToken) {

        String response = webClient.get()
                .uri(INFO_URL)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("kakaoGetUserInfo 에서 JSON 파싱 실패 = ", e);
        }

        UserDto userData = new UserDto();
        userData.setSocialId(rootNode.path("id").toString());
        userData.setSocialType("kakao");
        userData.setAccessToken(accessToken);
        userData.setEmail(rootNode.path("kakao_account").path("email").asText());
        userData.setName(rootNode.path("properties").path("nickname").asText());
        userData.setAvatar(rootNode.path("properties").path("thumbnail_image").asText());

        return userData;
    }

    public String kakaoUnLink(String accessToken) {

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
    }
}
