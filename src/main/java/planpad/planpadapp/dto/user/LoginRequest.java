package planpad.planpadapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotEmpty
    @Schema(description = "소셜 코드", example = "4a2f79d45d7f41e8b7a0 (필수)")
    private String code;

    @NotEmpty
    @Schema(description = "소셜 타입", example = "kakao (필수)")
    private String socialType;
}
