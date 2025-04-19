package planpad.planpadapp.dto.api.memo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.memo.MemoResponse;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemoResponseWrapper {
    private MemoResponse data;
    private String message;
}
