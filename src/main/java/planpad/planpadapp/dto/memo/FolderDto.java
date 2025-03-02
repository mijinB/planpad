package planpad.planpadapp.dto.memo;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.domain.User;

@Getter @Setter
public class FolderDto {

    @NotNull
    private User user;
    private int folderOrder;
    private String colorCode;
}
