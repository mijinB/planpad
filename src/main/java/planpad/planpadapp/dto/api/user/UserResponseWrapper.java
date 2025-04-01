package planpad.planpadapp.dto.api.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.user.UserInfoResponseDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseWrapper {
    private UserInfoResponseDto data;
    private String message;
}
