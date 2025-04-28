package planpad.planpadapp.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.calendar.enums.AnniversaryRecurrenceType;
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

    // Schedule
    public List<LocalDate> getOccurrencesBetween(ScheduleRecurrenceDto recurrence, LocalDate startDate, LocalDate endDate, LocalDate from, LocalDate to) {
        List<LocalDate> occurrences = new ArrayList<>();
        LocalDate current = from.isBefore(startDate) ? startDate : from;

        while (!current.isAfter(to) && (endDate == null || !current.isAfter(endDate))) {
            if (matchesRecurrence(recurrence, startDate, current)) {
                occurrences.add(current);
            }
            current = current.plusDays(1);
        }

        return occurrences;
    }

    private boolean matchesRecurrence(ScheduleRecurrenceDto recurrence, LocalDate startDate, LocalDate current) {
        ScheduleRecurrenceType type = recurrence.getRecurrenceType();

        switch (type) {
            case DAILY:
                long daysBetween = ChronoUnit.DAYS.between(startDate, current);
                return daysBetween % recurrence.getInterval() == 0;
            case WEEKDAYS:
                DayOfWeek dayOfWeek = current.getDayOfWeek();
                return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
            case WEEKLY:
                List<DayOfWeek> daysOfWeek = recurrence.getDaysOfWeek();
                return daysOfWeek != null && daysOfWeek.contains(current.getDayOfWeek());
            case MONTHLY:
                return matchesMonthly(recurrence, current);
            case YEARLY:
                return matchesYearly(recurrence, current);
            default:
                return false;
        }
    }

    private boolean matchesMonthly(ScheduleRecurrenceDto recurrence, LocalDate current) {
        Integer dayOfMonth = recurrence.getDayOfMonth();
        Integer weekOfMonth = recurrence.getWeekOfMonth();
        DayOfWeek dayOfWeek = recurrence.getDayOfWeek();

        if (dayOfMonth != null) {
            return dayOfMonth == current.getDayOfMonth();
        }
        if (weekOfMonth != null && dayOfWeek != null) {
            int fromWeekOfMonth = (current.getDayOfMonth() - 1) / 7 + 1;
            return weekOfMonth == fromWeekOfMonth && dayOfWeek == current.getDayOfWeek();
        }

        return false;
    }

    private boolean matchesYearly(ScheduleRecurrenceDto recurrence, LocalDate current) {
        Integer monthOfYear = recurrence.getMonthOfYear();
        Integer dayOfMonth = recurrence.getDayOfMonth();
        Integer weekOfMonth = recurrence.getWeekOfMonth();
        DayOfWeek dayOfWeek = recurrence.getDayOfWeek();

        if (monthOfYear != null && dayOfMonth != null) {
            return monthOfYear == current.getMonthValue()
                    && dayOfMonth == current.getDayOfMonth();
        }

        if (monthOfYear != null && weekOfMonth != null && dayOfWeek != null) {
            if (monthOfYear != current.getMonthValue()) return false;

            int fromWeekOfMonth = (current.getDayOfMonth() - 1) / 7 + 1;
            return weekOfMonth == fromWeekOfMonth && dayOfWeek == current.getDayOfWeek();
        }

        return false;
    }

    // Anniversary
    public List<LocalDate> getOccurrencesBetween(AnniversaryRecurrenceType type, LocalDate startDate, LocalDate endDate, LocalDate from, LocalDate to) {
        List<LocalDate> occurrences = new ArrayList<>();
        LocalDate current = from.isBefore(startDate) ? startDate : from;

        while (!current.isAfter(to) && (endDate == null || !current.isAfter(endDate))) {
            if (matchesAnniversaryRecurrence(type, startDate, current)) {
                occurrences.add(current);
            }
            current = current.plusDays(1);
        }

        return occurrences;
    }

    private boolean matchesAnniversaryRecurrence(AnniversaryRecurrenceType type, LocalDate startDate, LocalDate current) {
        long daysBetween = ChronoUnit.DAYS.between(startDate, current);

        switch (type) {
            case YEARLY:
                return startDate.getMonthValue() == current.getMonthValue()
                        && startDate.getDayOfMonth() == current.getDayOfMonth();
            case D100:
                return daysBetween % 100 == 0;
            case D1000:
                return daysBetween % 1000 == 0;
            default:
                return false;
        }
    }
}
