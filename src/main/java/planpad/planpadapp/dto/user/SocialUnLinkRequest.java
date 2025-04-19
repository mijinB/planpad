package planpad.planpadapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SocialUnLinkRequest {

    @NotEmpty
    @Schema(description = "소셜 타입", example = "kakao (필수)")
    private String socialType;
}
