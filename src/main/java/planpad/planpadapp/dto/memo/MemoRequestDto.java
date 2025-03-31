package planpad.planpadapp.dto.memo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemoRequestDto {

    @NotNull
    private Long folderId;

    private int memoOrder;

    @NotEmpty
    private String title;

    private String contents;

    private boolean isFixed;
}
