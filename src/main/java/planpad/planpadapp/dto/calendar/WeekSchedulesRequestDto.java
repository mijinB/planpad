package planpad.planpadapp.dto.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class WeekSchedulesRequestDto {

    @NotNull
    @Schema(description = "조회 시작 일자", example = "2025-03-30 (필수)")
    private LocalDate startDate;

    @NotNull
    @Schema(description = "조회 종료 일자", example = "2025-04-05 (필수)")
    private LocalDate endDate;
}
