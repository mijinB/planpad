package planpad.planpadapp.domain.calendar;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRecurrenceRule {

    private Integer interval;       // _일 | _번째 주 | _개월 마다 반복

    private Integer monthOfYear;    // _월

    private Integer weekOfMonth;    // _번째 주

    private Integer dayOfMonth;     // _일

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> daysOfWeek;
}
