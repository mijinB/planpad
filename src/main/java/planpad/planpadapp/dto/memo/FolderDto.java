package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.domain.Folder;
import planpad.planpadapp.domain.User;

@Getter @Setter
public class FolderDto {

    @NotEmpty
    @Schema(description = "폴더 이름", example = "folder (필수)")
    private String name;

    @NotEmpty
    @Schema(description = "폴더 색상 코드", example = "#FFFFFF (필수)")
    private String colorCode;

    @Schema(description = "폴더 순서", example = "1")
    private Integer folderOrder;

    public FolderDto() {}

    public FolderDto(Folder folder) {
        this.name = folder.getName();
        this.colorCode = folder.getColorCode();
        this.folderOrder = folder.getFolderOrder();
    }

    public Folder toEntity(User user, Integer nextOrder) {
        return new Folder(user, this.name, nextOrder, this.colorCode);
    }
}
