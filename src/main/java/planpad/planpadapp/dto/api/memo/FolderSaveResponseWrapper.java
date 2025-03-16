package planpad.planpadapp.dto.api.memo;

import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.dto.memo.FolderIdDto;

@Getter @Setter
public class FolderSaveResponseWrapper {
    private FolderIdDto data;
    private String message;
}
