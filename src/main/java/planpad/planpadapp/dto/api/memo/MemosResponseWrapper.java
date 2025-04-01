package planpad.planpadapp.dto.api.memo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.memo.MemosResponseDto;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemosResponseWrapper {
    private List<MemosResponseDto> data;
    private String message;
}
