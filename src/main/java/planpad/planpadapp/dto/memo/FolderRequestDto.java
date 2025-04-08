package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class FolderRequestDto {

    @NotEmpty
    @Size(min = 1, max = 20)
    @Schema(description = "폴더 이름", example = "folder (필수)")
    private String name;

    @NotEmpty
    @Schema(description = "폴더 색상 코드", example = "#FFFFFF (필수)")
    private String colorCode;
}
