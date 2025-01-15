package planpad.planpadapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.user.kakao.KakaoTokenResponseDto;
import planpad.planpadapp.dto.user.kakao.KakaoUserInfoDto;
import planpad.planpadapp.dto.user.kakao.KakaoUserRequestDto;
import planpad.planpadapp.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    @Operation(summary = "소셜 로그인", description = "소셜 로그인을 진행합니다.")
    public ResponseEntity<Map<String, Object>> SocialLogIn(@RequestBody @Valid KakaoUserRequestDto request) {

        Map<String, Object> responseBody = new HashMap<>();

        try {
            KakaoTokenResponseDto kakaoTokenResponse = userService.kakaoGetAccessToken(request);
            KakaoUserInfoDto kakaoUserInfo = userService.kakaoGetUserInfo(kakaoTokenResponse.getAccess_token());

            String userEmail = kakaoUserInfo.kakao_account.email;
            Optional<User> existingUserOptional = userService.findByEmail(userEmail);

            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();
                existingUser.setAccessToken(kakaoTokenResponse.getAccess_token());

                responseBody.put("data", existingUser);
            } else {
                User newUser = new User();
                newUser.setKakaoId(kakaoUserInfo.id);
                newUser.setAccessToken(kakaoTokenResponse.getAccess_token());
                newUser.setEmail(userEmail);
                newUser.setUserName(kakaoUserInfo.properties.nickname);
                newUser.setAvatar(kakaoUserInfo.properties.thumbnail_image);

                userService.join(newUser);
                responseBody.put("data", newUser);
            }

            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            responseBody.put("message", "정상적으로 처리하지 못했습니다.");
            log.info("exception = {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }
}
