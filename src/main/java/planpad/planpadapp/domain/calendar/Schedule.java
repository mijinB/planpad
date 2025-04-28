package planpad.planpadapp.domain.calendar;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.enums.ScheduleRecurrenceType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private CalendarGroup group;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "palette_id", nullable = false)
    private ColorPalette colorPalette;

    @NotNull
    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_type")
    private ScheduleRecurrenceType recurrenceType;

    @Embedded
    private ScheduleRecurrenceRule recurrenceRule;

    @NotEmpty
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder
    public Schedule(User user, CalendarGroup group, ColorPalette colorPalette, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, ScheduleRecurrenceType recurrenceType, ScheduleRecurrenceRule recurrenceRule, String title, String description) {
        this.user = user;
        this.group = group;
        this.colorPalette = colorPalette;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.recurrenceType = recurrenceType;
        this.recurrenceRule = recurrenceRule;
        this.title = title;
        this.description = description;
    }

    public void updateSchedule(CalendarGroup group, ColorPalette palette, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, ScheduleRecurrenceType recurrenceType, ScheduleRecurrenceRule recurrenceRule, String title, String description) {

        if (group != null) {
            this.group = group;
        }
        if (palette != null) {
            this.colorPalette = palette;
        }
        if (startDate != null) {
            this.startDate = startDate;
        }
        if (startTime != null) {
            this.startTime = startTime;
        }
        if (endDate != null) {
            this.endDate = endDate;
        }
        if (endTime != null) {
            this.endTime = endTime;
        }
        if (recurrenceType != null) {
            this.recurrenceType = recurrenceType;
        }
        if (recurrenceRule != null) {
            this.recurrenceRule = recurrenceRule;
        }
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
    }

    public Schedule copyWithNewStartDate(LocalDate newStartDate) {
        long daysBetween = ChronoUnit.DAYS.between(this.startDate, this.endDate);

        return Schedule.builder()
                .user(this.user)
                .group(this.group)
                .colorPalette(this.colorPalette)
                .startDate(newStartDate)
                .startTime(this.startTime)
                .endDate(newStartDate.plusDays(daysBetween))
                .recurrenceType(this.recurrenceType)
                .recurrenceRule(this.recurrenceRule)
                .title(this.title)
                .description(this.description)
                .build();
    }
}
