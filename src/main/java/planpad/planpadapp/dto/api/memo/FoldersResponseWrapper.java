package planpad.planpadapp.dto.api.memo;

import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.dto.memo.FoldersResponseDto;

import java.util.List;

@Getter @Setter
public class FoldersResponseWrapper {
    private List<FoldersResponseDto> data;
    private String message;

    public FoldersResponseWrapper(List<FoldersResponseDto> data, String message) {
        this.data = data;
        this.message = message;
    }
}
