package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.domain.memo.Folder;

@Getter
@NoArgsConstructor
public class FoldersResponse {

    @Schema(description = "폴더 id", example = "1")
    private Long id;

    @Schema(description = "폴더 이름", example = "folder")
    private String name;

    @Schema(description = "폴더 색상 코드", example = "#FFFFFF")
    private String colorCode;

    @Schema(description = "폴더 순서", example = "1")
    private Integer folderOrder;

    public FoldersResponse(Folder folder) {
        this.id = folder.getFolderId();
        this.name = folder.getName();
        this.colorCode = folder.getColorCode();
        this.folderOrder = folder.getFolderOrder();
    }
}
