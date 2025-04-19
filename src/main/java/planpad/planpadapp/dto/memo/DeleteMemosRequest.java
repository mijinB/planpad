package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class DeleteMemosRequest {

    @NotNull
    @Schema(description = "선택한 메모들의 id 리스트", example = "[1, 2, 3] (필수)")
    private List<Long> ids;
}
