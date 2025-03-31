package planpad.planpadapp.dto.api.memo;

import lombok.Getter;
import lombok.Setter;
import planpad.planpadapp.dto.memo.MemoResponseDto;

@Getter @Setter
public class MemoResponseWrapper {
    private MemoResponseDto data;
    private String message;

    public MemoResponseWrapper(MemoResponseDto data, String message) {
        this.data = data;
        this.message = message;
    }
}
