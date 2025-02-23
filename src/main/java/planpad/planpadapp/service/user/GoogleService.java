package planpad.planpadapp.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import planpad.planpadapp.dto.user.SocialUserDto;

import java.util.Map;

@Slf4j
@Service
public class GoogleService {

    @Value("${google.client_id}")
    private String CLIENT_ID;
    @Value("${google.client_secret}")
    private String CLIENT_SECRET;
    @Value("${google.redirect_uri}")
    private String REDIRECT_URL;
    @Value("${google.token_url}")
    private String TOKEN_URL;
    @Value("${google.info_url}")
    private String INFO_URL;
    @Value("${google.unlink_url}")
    private String UNLINK_URL;

    WebClient webClient = WebClient.create();

    public String googleGetAccessToken(String code) {

        Map<String, String> response = webClient.post()
                .uri(TOKEN_URL)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("grant_type=authorization_code" +
                        "&client_id=" + CLIENT_ID +
                        "&client_secret=" + CLIENT_SECRET +
                        "&redirect_uri=" + REDIRECT_URL +
                        "&code=" + code)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

        if (response == null || !response.containsKey("access_token")) {
            throw new RuntimeException("googleGetAccessToken 실패");
        }

        return response.get("access_token");
    }

    public SocialUserDto googleGetUserInfo(String accessToken) {

        Map<String, String> response = webClient.get()
                .uri(INFO_URL)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

        if (response == null) {
            throw new RuntimeException("googleGetUserInfo 실패");
        }

        SocialUserDto socialUser = new SocialUserDto();
        socialUser.setSocialId(response.get("id"));
        socialUser.setSocialType("google");
        socialUser.setAccessToken(accessToken);
        socialUser.setEmail(response.get("email"));
        socialUser.setName(response.get("name"));
        socialUser.setAvatar(response.get("picture"));

        return socialUser;
    }

    public void googleUnLink(String accessToken) {

        webClient.post()
                .uri(UNLINK_URL)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("client_id=" + CLIENT_ID +
                        "&client_secret=" + CLIENT_SECRET +
                        "&token=" + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
