package planpad.planpadapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import planpad.planpadapp.domain.User;
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
    public void saveUser(@RequestBody @Valid UserRequestDto request, @RequestHeader MultiValueMap<String, String> headerMap) {
        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setAvatar(request.getAvatar());

        log.info("headerMap = {}", headerMap);
        log.info("userName = {}, email = {}, avatar = {}", request.getUserName(), request.getEmail(), request.getAvatar());

        // userService.join(user);
    }
}
