package planpad.planpadapp.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.CalendarGroup;
import planpad.planpadapp.domain.calendar.ColorPalette;
import planpad.planpadapp.domain.calendar.Schedule;
import planpad.planpadapp.dto.calendar.ScheduleRequestDto;
import planpad.planpadapp.repository.calendar.ColorPaletteRepository;
import planpad.planpadapp.repository.calendar.ScheduleRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupService groupService;
    private final ColorPaletteRepository colorPaletteRepository;

    @Transactional
    public Long saveSchedule(User user, ScheduleRequestDto data) {

        CalendarGroup group = groupService.getAuthorizedGroupOrThrow(user, data.getGroupId());
        ColorPalette colorPalette = getAuthorizedPaletteOrThrow(user, data.getPaletteId());

        Schedule schedule = Schedule.builder()
                .user(user)
                .group(group)
                .colorPalette(colorPalette)
                .startDateTime(data.getStartDateTime())
                .endDateTime(data.getEndDateTime())
                .title(data.getTitle())
                .description(data.getDescription())
                .build();
        scheduleRepository.save(schedule);

        return schedule.getScheduleId();
    }

    public ColorPalette getAuthorizedPaletteOrThrow(User user, Long id) {
        ColorPalette colorPalette = colorPaletteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이 색상은 팔레트에 포함되어 있지 않습니다."));

        if (!colorPalette.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("이 색상 팔레트에 접근할 수 없습니다.");
        }

        return colorPalette;
    }
}
