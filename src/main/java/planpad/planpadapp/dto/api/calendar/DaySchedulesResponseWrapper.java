package planpad.planpadapp.dto.api.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.calendar.SchedulesResponseDto;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DaySchedulesResponseWrapper {
    private List<SchedulesResponseDto> data;
    private String message;
}
