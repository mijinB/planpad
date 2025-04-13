package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class MemoUpdateRequestDto {

    @Schema(description = "이동할 폴더 id", example = "1")
    private Long folderId;

    @Schema(description = "메모 제목", example = "memo title")
    private String title;

    @Schema(description = "메모 내용", example = "memo contents")
    private String contents;

    @Schema(description = "고정 여부", example = "true")
    private boolean isFixed;

    @Schema(description = "기존 메모 순서", example = "1")
    private Integer targetOrder;

    @Schema(description = "변경될 메모 순서", example = "5")
    private Integer nextOrder;
}
