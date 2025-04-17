package planpad.planpadapp.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.CalendarGroup;
import planpad.planpadapp.domain.calendar.ColorPalette;
import planpad.planpadapp.domain.calendar.Schedule;
import planpad.planpadapp.dto.calendar.MonthSchedulesRequestDto;
import planpad.planpadapp.dto.calendar.ScheduleRequestDto;
import planpad.planpadapp.dto.calendar.SchedulesResponseDto;
import planpad.planpadapp.dto.calendar.WeekSchedulesRequestDto;
import planpad.planpadapp.repository.calendar.ScheduleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Map<Integer, List<SchedulesResponseDto>> getSchedulesByMonth(User user, MonthSchedulesRequestDto data) {

        Stream<Schedule> filtered = user.getSchedules().stream()
                .filter(schedule -> {
                    LocalDateTime start = schedule.getStartDateTime();
                    return start.getYear() == data.getYear() && start.getMonthValue() == data.getMonth();
                });

        return toGroupedScheduleMap(filtered);
    }

    public Map<Integer, List<SchedulesResponseDto>> getSchedulesByWeek(User user, WeekSchedulesRequestDto data) {

        Stream<Schedule> filtered = user.getSchedules().stream()
                .filter(schedule -> {
                    LocalDate date = schedule.getStartDateTime().toLocalDate();
                    return (!date.isBefore(data.getStartDate())) && (!date.isAfter(data.getEndDate()));
                });

        return toGroupedScheduleMap(filtered);
    }

    private Map<Integer, List<SchedulesResponseDto>> toGroupedScheduleMap(Stream<Schedule> scheduleStream) {

        return scheduleStream
                .map(schedule -> {
                    String colorCode = schedule.getColorPalette().getColorCode();
                    int day = schedule.getStartDateTime().getDayOfMonth();
                    LocalTime startTime = schedule.getStartDateTime().toLocalTime();
                    LocalTime endTime = schedule.getEndDateTime().toLocalTime();

                    return Map.entry(day, new SchedulesResponseDto(colorCode, startTime, endTime, schedule.getTitle()));
                })
                .collect(Collectors.groupingBy(
                    Map.Entry::getKey,
                    Collectors.mapping(
                        Map.Entry::getValue,
                        Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> list.stream()
                                        .sorted(Comparator.comparing(SchedulesResponseDto::getStartTime))
                                        .collect(Collectors.toList())
                        )
                    )
                ));
    }
}
