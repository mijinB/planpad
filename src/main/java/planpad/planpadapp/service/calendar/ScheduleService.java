package planpad.planpadapp.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.CalendarGroup;
import planpad.planpadapp.domain.calendar.ColorPalette;
import planpad.planpadapp.domain.calendar.Schedule;
import planpad.planpadapp.domain.calendar.ScheduleRecurrenceRule;
import planpad.planpadapp.dto.calendar.schedule.*;
import planpad.planpadapp.repository.calendar.ScheduleRepository;

import java.time.LocalDate;
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
    private final RecurrenceService recurrenceService;

    @Transactional
    public Long createSchedule(User user, ScheduleRequest data) {

        CalendarGroup group = groupService.getAuthorizedGroupOrThrow(user, data.getGroupId());
        ColorPalette colorPalette = colorPaletteService.getAuthorizedPaletteOrThrow(user, data.getPaletteId());
        ScheduleRecurrenceRule recurrenceRule = new ScheduleRecurrenceRule(
                data.getRecurrence().getInterval(),
                data.getRecurrence().getMonthOfYear(),
                data.getRecurrence().getWeekOfMonth(),
                data.getRecurrence().getDayOfMonth(),
                data.getRecurrence().getDayOfWeek(),
                data.getRecurrence().getDaysOfWeek()
        );

        Schedule schedule = Schedule.builder()
                .user(user)
                .group(group)
                .colorPalette(colorPalette)
                .startDate(data.getStartDate())
                .startTime(data.getStartTime())
                .endDate(data.getEndDate())
                .endTime(data.getEndTime())
                .recurrenceType(data.getRecurrence().getRecurrenceType())
                .recurrenceRule(recurrenceRule)
                .title(data.getTitle())
                .description(data.getDescription())
                .build();
        scheduleRepository.save(schedule);

        return schedule.getScheduleId();
    }

    public Map<Integer, List<SchedulesResponse>> getSchedulesByMonth(User user, MonthSchedulesRequest data) {
        LocalDate monthStart = LocalDate.of(data.getYear(), data.getMonth(), 1);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());      // 다음달 1일 0시

        Stream<Schedule> singleSchedules = user.getSchedules().stream()
                .filter(schedule -> {
                    LocalDate start = schedule.getStartDate();
                    LocalDate end = schedule.getEndDate();
                    boolean isOverlapping = !start.isAfter(monthEnd) && !end.isBefore(monthStart);

                    return isOverlapping && isInGroup(data.getGroupIds(), schedule);
                });

        Stream<Schedule> recurringSchedules = user.getSchedules().stream()
                .filter(schedule -> schedule.getRecurrenceType() != null && isInGroup(data.getGroupIds(), schedule))
                .flatMap(schedule -> expandMonthlyOccurrences(schedule, monthStart, monthEnd).stream());

        Stream<Schedule> allSchedules = Stream.concat(singleSchedules, recurringSchedules);

        return toGroupedScheduleMap(allSchedules);
    }

    public Map<Integer, List<SchedulesResponse>> getSchedulesByWeek(User user, WeekSchedulesRequest data) {

        Stream<Schedule> filtered = user.getSchedules().stream()
                .filter(schedule -> {
                    LocalDate startDate = schedule.getStartDate();
                    LocalDate endDate = schedule.getEndDate();
                    boolean isOverlapping = !startDate.isAfter(data.getEndDate()) && !endDate.isBefore(data.getStartDate());

                    return isOverlapping && isInGroup(data.getGroupIds(), schedule);
                });

        return toGroupedScheduleMap(filtered);
    }

    public List<SchedulesResponse> getSchedulesByDay(User user, DaySchedulesRequest data) {

        return user.getSchedules().stream()
                .filter(schedule -> {
                    LocalDate startDate = schedule.getStartDate();
                    LocalDate endDate = schedule.getEndDate();
                    boolean isOverlapping = !startDate.isAfter(data.getDate()) && !endDate.isBefore(data.getDate());

                    return isOverlapping && isInGroup(data.getGroupIds(), schedule);
                })
                .map(schedule -> {
                    String colorCode = schedule.getColorPalette().getColorCode();
                    LocalTime startTime = schedule.getStartTime();
                    LocalTime endTime = schedule.getEndTime();

                    return new SchedulesResponse(colorCode, startTime, endTime, schedule.getTitle());
                })
                .sorted(Comparator.comparing(SchedulesResponse::getStartTime))
                .collect(Collectors.toList());
    }

    public ScheduleResponse getSchedule(User user, Long id) {
        Schedule schedule = getAuthorizedScheduleOrThrow(user, id);
        ScheduleRecurrenceDto recurrence = new ScheduleRecurrenceDto(
                schedule.getRecurrenceType(),
                schedule.getRecurrenceRule().getInterval(),
                schedule.getRecurrenceRule().getMonthOfYear(),
                schedule.getRecurrenceRule().getWeekOfMonth(),
                schedule.getRecurrenceRule().getDayOfMonth(),
                schedule.getRecurrenceRule().getDayOfWeek(),
                schedule.getRecurrenceRule().getDaysOfWeek()
        );

        return new ScheduleResponse(
                schedule.getGroup().getGroupId(),
                schedule.getColorPalette().getColorId(),
                schedule.getStartDate(),
                schedule.getStartTime(),
                schedule.getEndDate(),
                schedule.getEndTime(),
                recurrence,
                schedule.getTitle(),
                schedule.getDescription()
        );
    }

    @Transactional
    public void updateSchedule(User user, Long id, UpdateScheduleRequest data) {
        Schedule schedule = getAuthorizedScheduleOrThrow(user, id);
        CalendarGroup group = groupService.getAuthorizedGroupOrThrow(user, data.getGroupId());
        ColorPalette palette = colorPaletteService.getAuthorizedPaletteOrThrow(user, data.getPaletteId());
        ScheduleRecurrenceRule recurrenceRule = new ScheduleRecurrenceRule(
                data.getRecurrence().getInterval(),
                data.getRecurrence().getMonthOfYear(),
                data.getRecurrence().getWeekOfMonth(),
                data.getRecurrence().getDayOfMonth(),
                data.getRecurrence().getDayOfWeek(),
                data.getRecurrence().getDaysOfWeek()
        );

        schedule.updateSchedule(
                group,
                palette,
                data.getStartDate(),
                data.getStartTime(),
                data.getEndDate(),
                data.getEndTime(),
                data.getRecurrence().getRecurrenceType(),
                recurrenceRule,
                data.getTitle(),
                data.getDescription()
        );
    }

    @Transactional
    public void deleteSchedule(User user, Long id) {
        Schedule schedule = getAuthorizedScheduleOrThrow(user, id);
        scheduleRepository.delete(schedule);
    }

    private Map<Integer, List<SchedulesResponse>> toGroupedScheduleMap(Stream<Schedule> scheduleStream) {

        return scheduleStream
                .map(schedule -> {
                    String colorCode = schedule.getColorPalette().getColorCode();
                    int day = schedule.getStartDate().getDayOfMonth();
                    LocalTime startTime = schedule.getStartTime();
                    LocalTime endTime = schedule.getEndTime();

                    return Map.entry(day, new SchedulesResponse(colorCode, startTime, endTime, schedule.getTitle()));
                })
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                Map.Entry::getValue,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> list.stream()
                                                .sorted(Comparator.comparing(SchedulesResponse::getStartTime))
                                                .collect(Collectors.toList())
                                )
                        )
                ));
    }

    private boolean isInGroup(List<Long> groupIds, Schedule schedule) {

        return groupIds.stream()
                .anyMatch(groupId -> groupId.equals(schedule.getGroup().getGroupId()));
    }

    private List<Schedule> expandMonthlyOccurrences(Schedule schedule, LocalDate start, LocalDate end) {
        ScheduleRecurrenceDto recurrence = new ScheduleRecurrenceDto(
                schedule.getRecurrenceType(),
                schedule.getRecurrenceRule().getInterval(),
                schedule.getRecurrenceRule().getMonthOfYear(),
                schedule.getRecurrenceRule().getWeekOfMonth(),
                schedule.getRecurrenceRule().getDayOfMonth(),
                schedule.getRecurrenceRule().getDayOfWeek(),
                schedule.getRecurrenceRule().getDaysOfWeek()
        );

        List<LocalDate> occurrences = recurrenceService.getOccurrencesBetween(recurrence, schedule.getStartDate(), start, end);
        return occurrences.stream()
                .map(schedule::copyWithNewStartDateTime)
                .collect(Collectors.toList());
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
