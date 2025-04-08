package planpad.planpadapp.dto.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveResponseWrapper {
    private SaveResponseDto data;
    private String message;
}
