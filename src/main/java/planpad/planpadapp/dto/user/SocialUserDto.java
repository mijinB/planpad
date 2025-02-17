package planpad.planpadapp.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SocialUserDto {
    private String socialId;
    private String socialType;
    private String accessToken;
    private String email;
    private String name;
    private String avatar;
}
