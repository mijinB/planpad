package planpad.planpadapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import planpad.planpadapp.dto.user.TokenResponseDto;
import planpad.planpadapp.dto.user.UserRequestDto;
import planpad.planpadapp.service.UserService;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    @Operation(summary = "사용자 생성", description = "새 사용자를 생성합니다.")
    public void kakaoLogIn(@RequestBody @Valid UserRequestDto request) {

        TokenResponseDto tokenResponse = userService.getAccessToken(request);
        log.info("access_token = {}", tokenResponse.getAccess_token());

        /*User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setAvatar(request.getAvatar());

        userService.join(user);*/
    }
}
