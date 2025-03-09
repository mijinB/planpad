package planpad.planpadapp.dto.api.memo;

import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.dto.memo.FolderDto;

import java.util.List;

@Getter @Setter
public class FoldersResponseWrapper {
    private List<FolderDto> data;
    private String message;
}
