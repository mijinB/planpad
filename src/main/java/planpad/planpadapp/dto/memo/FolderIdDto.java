package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FolderIdDto {

    @NotNull
    @Schema(description = "폴더 id", example = "1")
    private Long id;
}
