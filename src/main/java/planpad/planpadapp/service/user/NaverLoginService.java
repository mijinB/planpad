package planpad.planpadapp.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import planpad.planpadapp.dto.user.naver.NaverTokenResponseDto;

@Slf4j
@Service
public class NaverLoginService {

    @Value("${naver.client_id}")
    private String CLIENT_ID;
    @Value("${naver.secret_value}")
    private String SECRET_VALUE;
    @Value("${naver.token_url}")
    private String TOKEN_URL;

    public String naverGetAccessToken(String code) {

        WebClient webClient = WebClient.create();
        ObjectMapper objectMapper = new ObjectMapper();

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

            NaverTokenResponseDto naverTokenResponseDto = objectMapper.readValue(response, NaverTokenResponseDto.class);
            return naverTokenResponseDto.getAccessToken();

        } catch (Exception e) {
            throw new RuntimeException("naverGetAccessToken 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
