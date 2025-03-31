package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.domain.Folder;

@Getter @Setter
public class FolderRequestDto {

    @NotNull
    @Schema(description = "폴더 id", example = "1")
    private Long id;

    @NotEmpty
    @Schema(description = "폴더 이름", example = "folder (필수)")
    private String name;

    @NotEmpty
    @Schema(description = "폴더 색상 코드", example = "#FFFFFF (필수)")
    private String colorCode;

    public FolderRequestDto() {}

    public FolderRequestDto(Folder folder) {
        this.id = folder.getFolderId();
        this.name = folder.getName();
        this.colorCode = folder.getColorCode();
    }
}
