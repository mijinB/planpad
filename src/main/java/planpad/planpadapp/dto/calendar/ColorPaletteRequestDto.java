package planpad.planpadapp.dto.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ColorPaletteRequestDto {

    @NotEmpty
    @Schema(description = "색상 팔레트 코드", example = "#FFFFFF (필수)")
    private String colorCode;

    @Schema(description = "색상 팔레트 이름", example = "운동")
    private String colorName;
}
