package planpad.planpadapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.user.TokenResponseDto;
import planpad.planpadapp.dto.user.UserRequestDto;
import planpad.planpadapp.repository.UserRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public TokenResponseDto getAccessToken(UserRequestDto request) {

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

            return objectMapper.readValue(response, TokenResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("getAccessToken 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Long join(User user) {
        userRepository.save(user);
        return user.getId();
    }

    public User findOne(Long userId) {
        return userRepository.findOne(userId);
    }
}
