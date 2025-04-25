package planpad.planpadapp.dto.calendar.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class WeekSchedulesRequest {

    @NotNull
    @Schema(description = "조회 그룹 id 리스트", example = "[1, 2, 3] (필수)")
    private List<Long> groupIds;

    @NotNull
    @Schema(description = "조회 시작 일자", example = "Date 타입 (필수)")
    private LocalDate startDate;

    @NotNull
    @Schema(description = "조회 종료 일자", example = "Date 타입 (필수)")
    private LocalDate endDate;
}
