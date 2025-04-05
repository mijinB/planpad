package planpad.planpadapp.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class MemoRequestDto {

    @NotNull(message = "폴더 id는 필수입니다.")
    @Schema(description = "폴더 id", example = "1 (필수)")
    private Long folderId;

    @NotEmpty(message = "폴더 제목은 필수입니다.")
    @Size(min = 1)
    @Schema(description = "메모 제목", example = "memo (필수)")
    private String title;

    @Schema(description = "메모 내용", example = "contents")
    private String contents;

    @Schema(description = "메모 고정 여부", example = "true (필수)")
    private boolean isFixed;

    @Schema(description = "메모 태그 리스트", example = "['tag1', 'tag2']")
    private List<String> tags;
}
