package planpad.planpadapp.dto.api.memo;

import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.dto.memo.FolderResponseDto;

import java.util.List;

@Getter @Setter
public class FoldersResponseWrapper {
    private List<FolderResponseDto> data;
    private String message;
}
