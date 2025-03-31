package planpad.planpadapp.dto.api.memo;

import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.dto.memo.MemosResponseDto;

import java.util.List;

@Getter @Setter
public class MemosResponseWrapper {
    private List<MemosResponseDto> data;
    private String message;
}
