package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateMemoRequest {

    @Schema(description = "이동할 폴더 id", example = "1")
    private Long folderId;

    @Schema(description = "메모 제목", example = "memo title")
    private String title;

    @Schema(description = "메모 내용", example = "memo content")
    private String content;

    @Schema(description = "고정 여부", example = "true")
    private Boolean fixed;

    @Schema(description = "메모 태그 리스트", example = "['tag1', 'tag2']")
    private List<String> tags;

    @Schema(description = "기존 메모 순서", example = "1")
    private Integer targetOrder;

    @Schema(description = "변경될 메모 순서", example = "5")
    private Integer nextOrder;
}
