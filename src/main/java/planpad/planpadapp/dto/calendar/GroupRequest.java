package planpad.planpadapp.dto.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class GroupRequest {

    @NotEmpty
    @Schema(description = "그룹 이름", example = "group")
    private String name;
}
