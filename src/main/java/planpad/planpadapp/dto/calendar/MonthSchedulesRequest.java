package planpad.planpadapp.dto.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class MonthSchedulesRequest {

    @NotNull
    @Schema(description = "조회 그룹 id 리스트", example = "[1, 2, 3] (필수)")
    private List<Long> groupIds;

    @NotNull
    @Schema(description = "조회 연도", example = "2025 (필수)")
    private Integer year;

    @NotNull
    @Schema(description = "조회 월", example = "4 (필수)")
    private Integer month;
}
