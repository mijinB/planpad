package planpad.planpadapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import planpad.planpadapp.dto.user.kakao.KakaoTokenResponseDto;
import planpad.planpadapp.dto.user.kakao.KakaoUserInfoDto;
import planpad.planpadapp.dto.user.kakao.KakaoUserRequestDto;
import planpad.planpadapp.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    @Operation(summary = "소셜 로그인", description = "소셜 로그인을 진행합니다.")
    public void SocialLogIn(@RequestBody @Valid KakaoUserRequestDto request) {

        try {
            KakaoTokenResponseDto tokenResponse = userService.kakaoGetAccessToken(request);
            KakaoUserInfoDto kakaoUserInfoDto = userService.kakaoGetUserInfo(tokenResponse.getAccess_token());
        } catch (Exception e) {
            log.info("exception = {}", e.getMessage());
        }

        /*User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setAvatar(request.getAvatar());

        userService.join(user);*/
    }
}
