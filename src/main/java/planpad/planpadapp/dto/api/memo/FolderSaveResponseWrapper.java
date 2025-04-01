package planpad.planpadapp.dto.api.memo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.memo.FolderResponseDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FolderSaveResponseWrapper {
    private FolderResponseDto data;
    private String message;
}
