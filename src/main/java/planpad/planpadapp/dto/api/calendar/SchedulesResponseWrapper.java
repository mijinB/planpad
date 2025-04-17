package planpad.planpadapp.dto.api.calendar;

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

    private Map<Integer, List<SchedulesResponseDto>> data;
    private String message;
}
