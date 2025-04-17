package planpad.planpadapp.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.ColorPalette;
import planpad.planpadapp.dto.calendar.ColorPaletteRequestDto;
import planpad.planpadapp.dto.calendar.ColorPaletteUpdateRequestDto;
import planpad.planpadapp.dto.calendar.ColorPalettesResponseDto;
import planpad.planpadapp.repository.calendar.ColorPaletteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ColorPaletteService {

    private final ColorPaletteRepository colorPaletteRepository;

    @Transactional
    public Long createColor(User user, ColorPaletteRequestDto data) {

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

        return colorPalette.getColorId();
    }

    public List<ColorPalettesResponseDto> getColors(User user) {

        return user.getColorPalettes().stream()
                .map(palette -> new ColorPalettesResponseDto(
                        palette.getColorId(),
                        palette.getColorCode(),
                        palette.getColorName()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateColor(User user, Long id, ColorPaletteUpdateRequestDto data) {
        ColorPalette colorPalette = getAuthorizedPaletteOrThrow(user, id);
        colorPalette.updateColor(data.getColorCode(), data.getColorName());
    }

    @Transactional
    public void deleteColor(User user, Long id) {
        ColorPalette colorPalette = getAuthorizedPaletteOrThrow(user, id);
        colorPaletteRepository.delete(colorPalette);
    }

    public ColorPalette getAuthorizedPaletteOrThrow(User user, Long id) {
        ColorPalette colorPalette = colorPaletteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이 색상은 팔레트에 포함되어 있지 않습니다."));

        if (!colorPalette.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("해당 색상 팔레트에 접근할 수 없습니다.");
        }

        return colorPalette;
    }
}
