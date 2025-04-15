package planpad.planpadapp.dto.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ColorPalettesResponseDto {

    @Schema(description = "색상 id", example = "1")
    private Long id;

    @Schema(description = "색상 코드", example = "#FFFFFF")
    private String colorCode;

    @Schema(description = "색상 이름", example = "운동")
    private String colorName;
}
