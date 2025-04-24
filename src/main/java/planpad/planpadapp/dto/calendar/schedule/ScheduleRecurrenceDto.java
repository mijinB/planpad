package planpad.planpadapp.dto.calendar.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.domain.calendar.enums.ScheduleRecurrenceType;

import java.time.DayOfWeek;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRecurrenceDto {

    @Schema(description = "기념일 반복 주기", example = "YEARLY or D100 or D1000 (中 1, 필수)")
    private ScheduleRecurrenceType recurrenceType;

    @Schema(description = "_일 | _번째 주 | _개월 마다 반복", example = "3")
    private Integer interval;

    @Schema(description = "_월", example = "12")
    private Integer monthOfYear;

    @Schema(description = "_번째 주", example = "2")
    private Integer weekOfMonth;

    @Schema(description = "_일", example = "25")
    private Integer dayOfMonth;

    @Schema(description = "_요일", example = "FRIDAY")
    private DayOfWeek dayOfWeek;

    @Schema(description = "_요일 다수 선택", example = "['MONDAY', 'WEDNESDAY', 'FRIDAY']")
    private List<DayOfWeek> daysOfWeek;
}
