package planpad.planpadapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.user.UserRequestDto;
import planpad.planpadapp.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @PostMapping("/users")
    public void saveUser(@RequestBody @Valid UserRequestDto request) {
        User user = new User();
        user.setUserName(request.getUserName());
        user.setAvatar(request.getAvatar());

        log.info("code = {}", request.getCode());

        userService.join(user);
    }
}
