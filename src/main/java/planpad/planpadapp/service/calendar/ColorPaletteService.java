package planpad.planpadapp.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.ColorPalette;
import planpad.planpadapp.dto.calendar.ColorPaletteRequest;
import planpad.planpadapp.dto.calendar.UpdateColorPaletteRequest;
import planpad.planpadapp.dto.calendar.ColorPalettesResponse;
import planpad.planpadapp.repository.calendar.ColorPaletteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ColorPaletteService {

    private final ColorPaletteRepository colorPaletteRepository;

    @Transactional
    public Long createColor(User user, ColorPaletteRequest data) {

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

    @Transactional
    public void createDefaultColor(User user) {
        List<String> colorCodes = List.of(
                "#b32839", "#bf5718", "#a87910", "#107a51", "#233fa1", "#5e1ba8", "#4b5059", "#e75871", "#de7043",
                "#e09e25", "#33a772", "#4b6add", "#8442d8", "#737a85", "#f88f9e", "#f1a182", "#eed160", "#7fd3a5", "#88a3f1",
                "#b78af1", "#a6abb2", "#fad5db", "#fce1d4", "#fdf3c6", "#cceddb", "#d3dffb", "#e9d7fb", "#cfd2d6");

        for (String code : colorCodes) {
            ColorPalette colorPalette = ColorPalette.builder()
                    .user(user)
                    .colorCode(code)
                    .colorName("")
                    .build();
            colorPaletteRepository.save(colorPalette);
        }
    }

    public List<ColorPalettesResponse> getColors(User user) {

        return user.getColorPalettes().stream()
                .map(palette -> new ColorPalettesResponse(
                        palette.getColorId(),
                        palette.getColorCode(),
                        palette.getColorName()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateColor(User user, Long id, UpdateColorPaletteRequest data) {
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
