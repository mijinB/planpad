package planpad.planpadapp.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.ColorPalette;
import planpad.planpadapp.dto.calendar.ColorPaletteRequestDto;
import planpad.planpadapp.repository.calendar.ColorPaletteRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ColorPaletteService {

    private final ColorPaletteRepository colorPaletteRepository;

    @Transactional
    public Long saveColorPalette(User user, ColorPaletteRequestDto data) {

        boolean exists = colorPaletteRepository.existsByUserAndColorCode(user, data.getColorCode());
        if (exists) {
            throw new IllegalArgumentException("이미 색상 팔레트에 포함된 색상입니다.");
        }

        ColorPalette colorPalette = ColorPalette.builder()
                .user(user)
                .colorCode(data.getColorCode())
                .colorName(data.getColorName())
                .build();
        colorPaletteRepository.save(colorPalette);

        return colorPalette.getPaletteId();
    }
}
