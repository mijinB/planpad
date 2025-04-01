package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class FolderRequestDto {

    @NotEmpty(message = "폴더 이름은 필수입니다.")
    @Size(min = 1, max = 20)
    @Schema(description = "폴더 이름", example = "folder (필수)")
    private String name;

    @NotEmpty(message = "폴더 색상 코드는 필수입니다.")
    @Schema(description = "폴더 색상 코드", example = "#FFFFFF (필수)")
    private String colorCode;
}
