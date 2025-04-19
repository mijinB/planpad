package planpad.planpadapp.dto.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class DaySchedulesRequest {

    @NotNull
    @Schema(description = "조회 그룹 id 리스트", example = "[1, 2, 3] (필수)")
    private List<Long> groupIds;

    @NotNull
    @Schema(description = "조회 일자", example = "2025-04-17 (필수)")
    private LocalDate date;
}
