package planpad.planpadapp.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponseDto {
    public String token;
    public String name;
    public String email;
    public String avatar;
}
