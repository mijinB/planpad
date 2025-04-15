package planpad.planpadapp.dto.api.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.calendar.ColorPalettesResponseDto;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ColorPalettesResponseWrapper {
    private List<ColorPalettesResponseDto> data;
    private String message;
}
