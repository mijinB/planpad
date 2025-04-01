package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TagRequestDto {

    @NotEmpty(message = "태그 이름은 필수입니다.")
    @Size(min = 1)
    @Schema(description = "태그 이름", example = "tag (필수)")
    private String name;
}
