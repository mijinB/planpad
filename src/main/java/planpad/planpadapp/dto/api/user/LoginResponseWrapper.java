package planpad.planpadapp.dto.api.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.user.LoginResponse;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseWrapper {
    private LoginResponse data;
    private String message;
}
