package planpad.planpadapp.dto.api.memo;

import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.dto.memo.MemoResponseDto;

import java.util.List;

@Getter @Setter
public class MemosResponseWrapper {
    private List<MemoResponseDto> data;
    private String message;
}
