package planpad.planpadapp.dto.api.user;

import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.dto.user.LoginResponseDto;

@Getter @Setter
public class LoginResponseWrapper {
    private LoginResponseDto data;
    private String message;

    public LoginResponseWrapper(LoginResponseDto data, String message) {
        this.data = data;
        this.message = message;
    }
}
