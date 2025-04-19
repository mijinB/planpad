package planpad.planpadapp.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.CalendarGroup;
import planpad.planpadapp.domain.calendar.ColorPalette;
import planpad.planpadapp.domain.calendar.Schedule;
import planpad.planpadapp.dto.calendar.*;
import planpad.planpadapp.repository.calendar.ScheduleRepository;

import java.awt.*;
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
                    boolean isSameMonth = start.getYear() == data.getYear() && start.getMonthValue() == data.getMonth();

                    return isSameMonth && isInGroup(data.getGroupIds(), schedule);
                });

        return toGroupedScheduleMap(filtered);
    }

    public Map<Integer, List<SchedulesResponseDto>> getSchedulesByWeek(User user, WeekSchedulesRequestDto data) {

        Stream<Schedule> filtered = user.getSchedules().stream()
                .filter(schedule -> {
                    LocalDate date = schedule.getStartDateTime().toLocalDate();
                    boolean isWithinRange = (!date.isBefore(data.getStartDate())) && (!date.isAfter(data.getEndDate()));

                    return isWithinRange && isInGroup(data.getGroupIds(), schedule);
                });

        return toGroupedScheduleMap(filtered);
    }

    public List<SchedulesResponseDto> getSchedulesByDay(User user, DaySchedulesRequestDto data) {

        return user.getSchedules().stream()
                .filter(schedule -> {
                    LocalDate date = schedule.getStartDateTime().toLocalDate();
                    boolean isSameDate = date.equals(data.getDate());

                    return isSameDate && isInGroup(data.getGroupIds(), schedule);
                })
                .map(schedule -> {
                    String colorCode = schedule.getColorPalette().getColorCode();
                    LocalTime startTime = schedule.getStartDateTime().toLocalTime();
                    LocalTime endTime = schedule.getEndDateTime().toLocalTime();

                    return new SchedulesResponseDto(colorCode, startTime, endTime, schedule.getTitle());
                })
                .sorted(Comparator.comparing(SchedulesResponseDto::getStartTime))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateSchedule(User user, Long id, ScheduleUpdateRequestDto data) {
        Schedule schedule = getAuthorizedScheduleOrThrow(user, id);
        CalendarGroup group = groupService.getAuthorizedGroupOrThrow(user, data.getGroupId());
        ColorPalette palette = colorPaletteService.getAuthorizedPaletteOrThrow(user, data.getPaletteId());

        schedule.updateSchedule(
                group,
                palette,
                data.getStartDateTime(),
                data.getEndDateTime(),
                data.getTitle(),
                data.getDescription()
        );
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

    private boolean isInGroup(List<Long> groupIds, Schedule schedule) {

        return groupIds.stream()
                .anyMatch(groupId -> groupId.equals(schedule.getGroup().getGroupId()));
    }

    private Schedule getAuthorizedScheduleOrThrow(User user, Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다."));

        if (!schedule.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("해당 일정에 접근할 수 없습니다.");
        }

        return schedule;
    }
}
