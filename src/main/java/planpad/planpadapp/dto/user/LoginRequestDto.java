package planpad.planpadapp.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @NotEmpty
    private String code;

    @NotEmpty
    private String socialType;
}
