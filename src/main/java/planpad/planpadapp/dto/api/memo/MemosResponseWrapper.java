package planpad.planpadapp.dto.api.memo;

import lombok.Getter;
import planpad.planpadapp.dto.memo.MemosResponseDto;

import java.util.List;

@Getter
public class MemosResponseWrapper {
    private List<MemosResponseDto> data;
    private String message;
}
