package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UpdateFolderRequest {

    @Schema(description = "폴더 이름", example = "folder")
    private String name;

    @Schema(description = "폴더 색상 코드", example = "#FFFFFF")
    private String colorCode;

    @Schema(description = "기존 폴더 순서", example = "1")
    private Integer targetOrder;

    @Schema(description = "변경될 폴더 순서", example = "5")
    private Integer nextOrder;
}
