package planpad.planpadapp.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.Anniversary;
import planpad.planpadapp.domain.calendar.CalendarGroup;
import planpad.planpadapp.domain.calendar.ColorPalette;
import planpad.planpadapp.dto.calendar.anniversary.AnniversaryRequest;
import planpad.planpadapp.repository.calendar.AnniversaryRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnniversaryService {

    private final AnniversaryRepository anniversaryRepository;
    private final GroupService groupService;
    private final ColorPaletteService colorPaletteService;

    @Transactional
    public Long createAnniversary(User user, AnniversaryRequest data) {

        CalendarGroup group = groupService.getAuthorizedGroupOrThrow(user, data.getGroupId());
        ColorPalette colorPalette = colorPaletteService.getAuthorizedPaletteOrThrow(user, data.getPaletteId());

        Anniversary anniversary = Anniversary.builder()
                .user(user)
                .group(group)
                .colorPalette(colorPalette)
                .startDate(data.getStartDate())
                .endDate(data.getEndDate())
                .recurrenceType(data.getRecurrenceType())
                .title(data.getTitle())
                .build();
        anniversaryRepository.save(anniversary);

        return anniversary.getAnniversaryId();
    }

    private Anniversary getAuthorizedAnniversaryOrThrow (User user, Long id) {
        Anniversary anniversary = anniversaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("기념일을 찾을 수 없습니다."));

        if (!anniversary.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("해당 기념일에 접근할 수 없습니다.");
        }

        return anniversary;
    }
}
