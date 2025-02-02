package planpad.planpadapp.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SocialUnLinkRequestDto {

    @NotEmpty
    private String socialType;
}
