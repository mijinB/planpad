package planpad.planpadapp.dto.api.memo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.memo.FoldersResponseDto;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FoldersResponseWrapper {
    private List<FoldersResponseDto> data;
    private String message;
}
