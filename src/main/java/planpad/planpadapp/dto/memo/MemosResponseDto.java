package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Schema(description = "메모 조회 응답 데이터")
public class MemosResponseDto {

    @Schema(description = "태그 리스트", example = "['A', 'B', 'C']")
    public List<String> tags;

    @Schema(description = "제목", example = "메모 제목")
    public String title;

    @Schema(description = "내용", example = "메모 내용")
    public String contents;

    @Schema(description = "고정 여부", example = "true")
    public boolean isFixed;
}
