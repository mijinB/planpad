package planpad.planpadapp.dto.api.user;

import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.dto.user.LoginResponseDto;

@Getter @Setter
public class LoginResponseWrapper {
    private LoginResponseDto data;
    private String message;
}
