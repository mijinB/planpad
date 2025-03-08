package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.domain.Folder;
import planpad.planpadapp.domain.User;

@Getter @Setter
public class FolderDto {

    @NotEmpty
    @Schema(description = "폴더 이름", example = "folder (필수)")
    private String name;

    @Schema(description = "폴더 순서", example = "1")
    private Integer folderOrder;

    @Schema(description = "폴더 색상 코드", example = "#DCDCDC")
    private String colorCode;

    public Folder toEntity(User user) {
        return new Folder(user, this.name, this.folderOrder, this.colorCode);
    }
}
