package planpad.planpadapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
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
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
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
