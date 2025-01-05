package planpad.planpadapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import planpad.planpadapp.dto.user.kakao.KakaoUserInfoDto;
import planpad.planpadapp.dto.user.kakao.TokenResponseDto;
import planpad.planpadapp.dto.user.UserRequestDto;
import planpad.planpadapp.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    @Operation(summary = "사용자 생성", description = "새 사용자를 생성합니다.")
    public void kakaoLogIn(@RequestBody @Valid UserRequestDto request) {

        try {
            TokenResponseDto tokenResponse = userService.getAccessToken(request);
            KakaoUserInfoDto kakaoUserInfoDto = userService.getUserInfo(tokenResponse.getAccess_token());
            log.info("kakaoUserInfoDto email = {}", kakaoUserInfoDto.kakao_account.email);
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
