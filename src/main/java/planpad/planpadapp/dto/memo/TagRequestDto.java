package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TagRequestDto {

    @NotEmpty
    @Size(min = 1)
    @Schema(description = "태그 이름", example = "tag (필수)")
    private String name;
}
