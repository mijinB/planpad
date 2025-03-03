package planpad.planpadapp.dto.memo;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.domain.Folder;
import planpad.planpadapp.domain.Tag;
import planpad.planpadapp.domain.User;

import java.util.List;

@Getter @Setter
public class MemoDto {

    @NotNull
    private User user;

    @NotNull
    private Folder folder;

    private List<Tag> tags;
    private int memoOrder;

    @NotEmpty
    private String title;

    private String contents;
    private boolean isFixed;
}
