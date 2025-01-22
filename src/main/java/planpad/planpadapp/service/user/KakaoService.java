package planpad.planpadapp.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import planpad.planpadapp.dto.user.kakao.KakaoTokenResponseDto;
import planpad.planpadapp.dto.user.kakao.KakaoUserInfoDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    @Value("${kakao.token_url}")
    private String TOKEN_URL;
    @Value("${kakao.info_url}")
    private String INFO_URL;
    @Value("${kakao.logout_url}")
    private String LOGOUT_URL;
    @Value("${kakao.client_id}")
    private String CLIENT_ID;
    @Value("${kakao.redirect_uri}")
    private String REDIRECT_URI;

    public String kakaoGetAccessToken(String code) {

        WebClient webClient = WebClient.create();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String response = webClient.post()
                    .uri(TOKEN_URL)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .bodyValue("grant_type=authorization_code" +
                            "&client_id=" + CLIENT_ID +
                            "&redirect_uri=" + REDIRECT_URI +
                            "&code=" + code)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();       // 동기적으로 응답 값을 반환
            KakaoTokenResponseDto tokenResponse = objectMapper.readValue(response, KakaoTokenResponseDto.class);

            return tokenResponse.getAccess_token();

        } catch (Exception e) {
            throw new RuntimeException("getAccessToken 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public KakaoUserInfoDto kakaoGetUserInfo(String accessToken) {

        WebClient webClient = WebClient.create();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String response = webClient.get()
                    .uri(INFO_URL)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return objectMapper.readValue(response, KakaoUserInfoDto.class);
        } catch (Exception e) {
            throw new RuntimeException("getUserInfo 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public void kakaoLogout(String accessToken) {
        WebClient webClient = WebClient.create();

        try {
            String response = webClient.post()
                    .uri(LOGOUT_URL)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("kakaoLogout 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
