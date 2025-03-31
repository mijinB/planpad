package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemoResponseDto {

    @Schema(description = "메모 id", example = "1")
    public Long id;
}
