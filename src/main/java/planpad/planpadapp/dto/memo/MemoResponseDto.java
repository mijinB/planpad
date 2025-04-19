package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MemoResponseDto {

    @Schema(description = "메모 id", example = "1")
    private Long id;

    @Schema(description = "폴더 id", example = "1")
    private Long folderId;

    @Schema(description = "태그 리스트", example = "['A', 'B', 'C']")
    private List<String> tags;

    @Schema(description = "제목", example = "메모 제목")
    private String title;

    @Schema(description = "내용", example = "메모 내용")
    private String content;

    @Schema(description = "고정 여부", example = "true")
    private boolean isFixed;

    public MemoResponseDto(Long id, Long folderId, List<String> tags, String title, String content, boolean isFixed) {
        this.id = id;
        this.folderId = folderId;
        this.tags = tags;
        this.title = title;
        this.content = content;
        this.isFixed = isFixed;
    }
}
