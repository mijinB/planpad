package planpad.planpadapp.dto.api.memo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.memo.MemosResponse;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemosResponseWrapper {
    private List<MemosResponse> data;
    private String message;
}
