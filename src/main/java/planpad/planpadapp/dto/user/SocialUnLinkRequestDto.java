package planpad.planpadapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SocialUnLinkRequestDto {

    @NotEmpty(message = "소셜 타입은 필수입니다.")
    @Schema(description = "소셜 타입", example = "kakao (필수)")
    private String socialType;
}
