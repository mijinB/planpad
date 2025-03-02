package planpad.planpadapp.dto.memo;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.domain.Folder;
import planpad.planpadapp.domain.User;

@Getter @Setter
public class MemoDto {

    @NotNull
    private User user;

    @NotNull
    private Folder folder;

    private int memoOrder;

    @NotEmpty
    private String title;

    private String contents;
    private JsonNode tags;
    private boolean isFixed;
}
