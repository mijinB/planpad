package planpad.planpadapp.dto.api.memo;

import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.dto.memo.FolderResponseDto;

@Getter @Setter
public class FolderSaveResponseWrapper {
    private FolderResponseDto data;
    private String message;

    public FolderSaveResponseWrapper(FolderResponseDto data, String message) {
        this.data = data;
        this.message = message;
    }
}
