package planpad.planpadapp.dto.user.kakao;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class KakaoUserRequestDto {

    @NotEmpty
    private String code;
}
