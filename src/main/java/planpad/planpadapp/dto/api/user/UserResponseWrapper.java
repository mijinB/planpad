package planpad.planpadapp.dto.api.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.user.UserInfoResponse;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseWrapper {
    private UserInfoResponse data;
    private String message;
}
