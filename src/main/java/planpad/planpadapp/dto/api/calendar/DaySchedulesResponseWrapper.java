package planpad.planpadapp.dto.api.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.calendar.SchedulesResponse;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DaySchedulesResponseWrapper {
    private List<SchedulesResponse> data;
    private String message;
}
