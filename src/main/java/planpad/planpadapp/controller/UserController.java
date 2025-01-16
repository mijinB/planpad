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
import planpad.planpadapp.dto.user.kakao.KakaoUserRequestDto;
import planpad.planpadapp.service.UserService;

import java.util.HashMap;
import java.util.Map;

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
            User responseUser = userService.kakaoLogin(request.getCode());

            responseBody.put("data", responseUser);
            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            responseBody.put("message", "정상적으로 처리하지 못했습니다.");
            log.info("exception = {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }
}
