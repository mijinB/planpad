package planpad.planpadapp.dto.api.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.user.LoginResponseDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseWrapper {
    private LoginResponseDto data;
    private String message;
}
