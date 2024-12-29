package planpad.planpadapp.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRequestDto {

    @NotEmpty
    private String code;
}
