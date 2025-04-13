package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MemosResponseDto {

    @Schema(description = "메모 id", example = "1")
    private Long id;

    @Schema(description = "폴더 id", example = "1")
    private Long folderId;

    @Schema(description = "메모 순서", example = "1")
    private Integer memoOrder;

    @Schema(description = "태그 리스트", example = "['A', 'B', 'C']")
    private List<String> tags;

    @Schema(description = "제목", example = "메모 제목")
    private String title;

    @Schema(description = "내용", example = "메모 내용")
    private String contents;

    @Schema(description = "고정 여부", example = "true")
    private boolean isFixed;

    public MemosResponseDto(Long id, Long folderId, Integer memoOrder, List<String> tags, String title, String contents, boolean isFixed) {
        this.id = id;
        this.folderId = folderId;
        this.memoOrder = memoOrder;
        this.tags = tags;
        this.title = title;
        this.contents = contents;
        this.isFixed = isFixed;
    }
}
