package planpad.planpadapp.dto.memo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class MemoRequestDto {

    @NotNull(message = "폴더 id는 필수입니다.")
    private Long folderId;

    private int memoOrder;

    @NotEmpty(message = "폴더 제목은 필수입니다.")
    @Size(min = 1)
    private String title;

    private String contents;

    private boolean isFixed;

    private List<String> tags;
}
