package planpad.planpadapp.dto.api.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulesResponseWrapper {

    private Map<Integer, List<Object>> data;
    private String message;
}
