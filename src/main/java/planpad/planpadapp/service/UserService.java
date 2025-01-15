package planpad.planpadapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.user.kakao.KakaoTokenResponseDto;
import planpad.planpadapp.dto.user.kakao.KakaoUserInfoDto;
import planpad.planpadapp.dto.user.kakao.KakaoUserRequestDto;
import planpad.planpadapp.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public KakaoTokenResponseDto kakaoGetAccessToken(KakaoUserRequestDto request) {

        WebClient webClient = WebClient.create();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
            String CLIENT_ID = "97106bc8684af5584543581289cfd304";
            String REDIRECT_URI = "http://localhost:3000/api/auth/kakao";
            String code = request.getCode();

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

            return objectMapper.readValue(response, KakaoTokenResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("getAccessToken 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public KakaoUserInfoDto kakaoGetUserInfo(String accessToken) {

        WebClient webClient = WebClient.create();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String INFO_URL = "https://kapi.kakao.com/v2/user/me";

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

    @Transactional
    public Long join(User user) {
        userRepository.save(user);
        return user.getId();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
