package planpad.planpadapp.dto.api;

import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.dto.user.UserResponseDto;

@Getter @Setter
public class UserResponseWrapper {
    private UserResponseDto data;
    private String message;
}
