package planpad.planpadapp.dto.api.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.calendar.SchedulesResponseDto;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulesResponseWrapper {

    @Schema(description = "일정 조회 데이터", example = "{1: [...], 2: [...], ...}")
    private Map<Integer, List<SchedulesResponseDto>> data;
    private String message;
}
