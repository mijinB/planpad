package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FolderResponseDto {

    @Schema(description = "폴더 id", example = "1")
    private Long id;

    public FolderResponseDto(Long id) {
        this.id = id;
    }
}
