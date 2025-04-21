package planpad.planpadapp.dto.api.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.calendar.anniversary.AnniversariesResponse;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnniversariesResponseWrapper {
    private List<AnniversariesResponse> data;
    private String message;
}
