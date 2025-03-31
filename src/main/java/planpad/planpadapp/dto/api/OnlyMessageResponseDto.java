package planpad.planpadapp.dto.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OnlyMessageResponseDto {
    private String message;

    public OnlyMessageResponseDto(String message) {
        this.message = message;
    }
}
