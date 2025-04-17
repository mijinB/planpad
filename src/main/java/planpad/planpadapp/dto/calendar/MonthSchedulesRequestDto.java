package planpad.planpadapp.dto.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MonthSchedulesRequestDto {

    @NotNull
    @Schema(description = "조회 연도", example = "2025 (필수)")
    private Integer year;

    @NotNull
    @Schema(description = "조회 월", example = "4 (필수)")
    private Integer month;
}
