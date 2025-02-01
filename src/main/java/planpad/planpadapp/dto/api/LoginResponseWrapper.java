package planpad.planpadapp.dto.api;

import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.dto.user.LoginResponseDto;

@Getter @Setter
public class LoginResponseWrapper {
    private LoginResponseDto data;
    private String message;
}
