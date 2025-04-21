package planpad.planpadapp.dto.api.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.calendar.anniversary.AnniversaryResponse;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnniversaryResponseWrapper {
    private AnniversaryResponse data;
    private String message;
}
