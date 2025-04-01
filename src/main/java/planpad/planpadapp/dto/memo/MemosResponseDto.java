package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.domain.Memo;
import planpad.planpadapp.domain.Tag;

import java.util.Set;

@Getter
@NoArgsConstructor
@Schema(description = "메모 조회 응답 데이터")
public class MemosResponseDto {

    @Schema(description = "태그 리스트", example = "['A', 'B', 'C']")
    private Set<Tag> tags;

    @Schema(description = "제목", example = "메모 제목")
    private String title;

    @Schema(description = "내용", example = "메모 내용")
    private String contents;

    @Schema(description = "고정 여부", example = "true")
    private boolean isFixed;

    public MemosResponseDto(Memo memo) {
        this.tags = memo.getTags();
        this.title = memo.getTitle();
        this.contents = memo.getContents();
        this.isFixed = memo.isFixed();
    }
}
