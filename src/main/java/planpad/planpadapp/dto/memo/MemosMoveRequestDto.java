package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class MemosMoveRequestDto {

    @NotNull
    @Schema(description = "이동할 폴더 id", example = "1 (필수)")
    private Long folderId;

    @NotNull
    @Schema(description = "선택한 메모들의 id 리스트", example = "[1, 2, 3] (필수)")
    private List<Long> memoIds;
}
