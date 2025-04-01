package planpad.planpadapp.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotNull
    private String socialId;

    @NotEmpty
    private String socialType;

    @NotEmpty
    private String accessToken;

    @NotEmpty
    private String email;

    private String name;
    private String avatar;
}
