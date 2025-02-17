package planpad.planpadapp.service.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import planpad.planpadapp.dto.user.SocialUserDto;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@Slf4j
@Service
public class NaverLoginService {

    @Value("${naver.client_id}")
    private String CLIENT_ID;
    @Value("${naver.secret_value}")
    private String SECRET_VALUE;
    @Value("${naver.token_url}")
    private String TOKEN_URL;
    @Value("${naver.info_url}")
    private String INFO_URL;

    WebClient webClient = WebClient.create();
    ObjectMapper objectMapper = new ObjectMapper();

    public String naverGetAccessToken(String code) {

        try {
            String response = webClient.post()
                    .uri(TOKEN_URL)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .bodyValue("grant_type=authorization_code" +
                            "&client_id=" + CLIENT_ID +
                            "&client_secret=" + SECRET_VALUE +
                            "&code=" + code)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            Map<String, String> responseMap = objectMapper.readValue(response, Map.class);
            return responseMap.get("access_token");

        } catch (Exception e) {
            throw new RuntimeException("naverGetAccessToken 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public SocialUserDto naverGetUserInfo(String accessToken) {

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

            try {
                socialUser.setSocialId(rootNode.path("response").path("id").asText());
                socialUser.setSocialType("naver");
                socialUser.setAccessToken(accessToken);
                socialUser.setEmail(rootNode.path("response").path("email").asText());
                socialUser.setName(URLDecoder.decode(rootNode.path("response").path("name").asText(), "UTF-8"));
                socialUser.setAvatar(rootNode.path("response").path("profile_image").asText());

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }

            return socialUser;

        } catch (Exception e) {
            throw new RuntimeException("naverGetUserInfo 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public String naverUnLink(String accessToken) {

        try {
            String response = webClient.post()
                    .uri(TOKEN_URL)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .bodyValue("grant_type=delete" +
                            "&client_id=" + CLIENT_ID +
                            "&client_secret=" + SECRET_VALUE +
                            "&access_token=" + accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            Map<String, String> responseMap = objectMapper.readValue(response, Map.class);
            return responseMap.get("access_token");

        } catch (Exception e) {
            throw new RuntimeException("kakaoUnLink 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
