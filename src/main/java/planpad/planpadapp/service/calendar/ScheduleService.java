package planpad.planpadapp.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.CalendarGroup;
import planpad.planpadapp.domain.calendar.ColorPalette;
import planpad.planpadapp.domain.calendar.Schedule;
import planpad.planpadapp.dto.calendar.ScheduleRequestDto;
import planpad.planpadapp.repository.calendar.ScheduleRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupService groupService;
    private final ColorPaletteService colorPaletteService;

    @Transactional
    public Long createSchedule(User user, ScheduleRequestDto data) {

        CalendarGroup group = groupService.getAuthorizedGroupOrThrow(user, data.getGroupId());
        ColorPalette colorPalette = colorPaletteService.getAuthorizedPaletteOrThrow(user, data.getPaletteId());

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
}
