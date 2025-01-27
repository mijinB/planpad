package planpad.planpadapp.dto.common;

import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.dto.user.UserResponseDto;

@Getter @Setter
public class OkResponseWrapper {
    private UserResponseDto data;
    private String message;
}
