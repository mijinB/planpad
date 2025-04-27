package planpad.planpadapp.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.calendar.enums.ScheduleRecurrenceType;
import planpad.planpadapp.dto.calendar.schedule.ScheduleRecurrenceDto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecurrenceService {

    public List<LocalDate> getOccurrencesBetween(ScheduleRecurrenceDto recurrence, LocalDate startDate, LocalDate from, LocalDate to) {
        List<LocalDate> occurrences = new ArrayList<>();

        LocalDate current = from;

        while (!current.isAfter(to)) {
            if (matchesRecurrence(recurrence, startDate, from)) {
                occurrences.add(current);
            }
            current = current.plusDays(1);
        }

        return occurrences;
    }

    private boolean matchesRecurrence(ScheduleRecurrenceDto recurrence, LocalDate startDate, LocalDate from) {
        ScheduleRecurrenceType type = recurrence.getRecurrenceType();

        switch (type) {
            case DAILY:
                long daysBetween = ChronoUnit.DAYS.between(startDate, from);
                return daysBetween % recurrence.getInterval() == 0;
            case WEEKDAYS:
                DayOfWeek dayOfWeek = from.getDayOfWeek();
                return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
            case WEEKLY:
                List<DayOfWeek> daysOfWeek = recurrence.getDaysOfWeek();
                return daysOfWeek != null && daysOfWeek.contains(from.getDayOfWeek());
            case MONTHLY:
                return matchesMonthly(recurrence, from);
            case YEARLY:
                return matchesYearly(recurrence, from);
            default:
                return false;
        }
    }

    private boolean matchesMonthly(ScheduleRecurrenceDto recurrence, LocalDate from) {
        Integer dayOfMonth = recurrence.getDayOfMonth();
        Integer weekOfMonth = recurrence.getWeekOfMonth();
        DayOfWeek dayOfWeek = recurrence.getDayOfWeek();

        if (dayOfMonth != null) {
            return dayOfMonth == from.getDayOfMonth();
        }
        if (weekOfMonth != null && dayOfWeek != null) {
            int fromWeekOfMonth = (from.getDayOfMonth() - 1) / 7 + 1;
            return weekOfMonth == fromWeekOfMonth && dayOfWeek == from.getDayOfWeek();
        }

        return false;
    }

    private boolean matchesYearly(ScheduleRecurrenceDto recurrence, LocalDate from) {
        Integer monthOfYear = recurrence.getMonthOfYear();
        Integer dayOfMonth = recurrence.getDayOfMonth();
        Integer weekOfMonth = recurrence.getWeekOfMonth();
        DayOfWeek dayOfWeek = recurrence.getDayOfWeek();

        if (monthOfYear != null && dayOfMonth != null) {
            return monthOfYear == from.getMonthValue()
                    && dayOfMonth == from.getDayOfMonth();
        }

        if (monthOfYear != null && weekOfMonth != null && dayOfWeek != null) {
            if (monthOfYear != from.getMonthValue()) return false;

            int fromWeekOfMonth = (from.getDayOfMonth() - 1) / 7 + 1;
            return weekOfMonth == fromWeekOfMonth && dayOfWeek == from.getDayOfWeek();
        }

        return false;
    }
}
