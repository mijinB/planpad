package planpad.planpadapp.dto.api.memo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.memo.MemoResponseDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemoResponseWrapper {
    private MemoResponseDto data;
    private String message;
}
