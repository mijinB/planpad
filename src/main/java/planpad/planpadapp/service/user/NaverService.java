package planpad.planpadapp.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import planpad.planpadapp.dto.user.UserDto;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@Service
public class NaverService {

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

        Map<String, String> response = webClient.post()
                .uri(TOKEN_URL)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .bodyValue("grant_type=authorization_code" +
                        "&client_id=" + CLIENT_ID +
                        "&client_secret=" + SECRET_VALUE +
                        "&code=" + code)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

        if (response == null || !response.containsKey("access_token")) {
            throw new RuntimeException("naverGetAccessToken 실패");
        }

        return response.get("access_token");
    }

    public UserDto naverGetUserInfo(String accessToken) {

        String response = webClient.get()
                .uri(INFO_URL)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode rootNode;
        UserDto socialUser = new UserDto();

        try {
            rootNode = objectMapper.readTree(response);
            socialUser.setName(URLDecoder.decode(rootNode.path("response").path("name").asText(), "UTF-8"));

        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new RuntimeException("naverGetUserInfo 처리 중 오류 발생: " + e.getMessage(), e);
        }

        socialUser.setSocialId(rootNode.path("response").path("id").asText());
        socialUser.setSocialType("naver");
        socialUser.setAccessToken(accessToken);
        socialUser.setEmail(rootNode.path("response").path("email").asText());
        socialUser.setAvatar(rootNode.path("response").path("profile_image").asText());

        return socialUser;
    }

    public String naverUnLink(String accessToken) {

        Map<String, String> response = webClient.post()
                .uri(TOKEN_URL)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .bodyValue("grant_type=delete" +
                        "&client_id=" + CLIENT_ID +
                        "&client_secret=" + SECRET_VALUE +
                        "&access_token=" + accessToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

        if (response == null || !response.containsKey("access_token")) {
            throw new RuntimeException("naverUnLink 실패");
        }

        return response.get("access_token");
    }
}
