package planpad.planpadapp.dto.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponseDto {

    @Schema(description = "그룹 id", example = "1")
    private Long id;
}
