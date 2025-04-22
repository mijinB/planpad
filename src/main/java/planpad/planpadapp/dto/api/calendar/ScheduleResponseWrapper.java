package planpad.planpadapp.dto.api.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.calendar.schedule.ScheduleResponse;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseWrapper {
    private ScheduleResponse data;
    private String message;
}
