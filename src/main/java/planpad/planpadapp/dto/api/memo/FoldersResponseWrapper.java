package planpad.planpadapp.dto.api.memo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.memo.FoldersResponse;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FoldersResponseWrapper {
    private List<FoldersResponse> data;
    private String message;
}
