package planpad.planpadapp.dto.api.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.calendar.ColorPalettesResponse;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ColorPalettesResponseWrapper {
    private List<ColorPalettesResponse> data;
    private String message;
}
