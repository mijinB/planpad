package planpad.planpadapp.dto.api.user;

import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.dto.user.UserInfoResponseDto;

@Getter @Setter
public class UserResponseWrapper {
    private UserInfoResponseDto data;
    private String message;

    public UserResponseWrapper(UserInfoResponseDto data, String message) {
        this.data = data;
        this.message = message;
    }
}
