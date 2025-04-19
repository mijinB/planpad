package planpad.planpadapp.domain.calendar;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.domain.User;

import java.time.LocalDateTime;

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
    @Column(name = "start_datetime")
    private LocalDateTime startDateTime;

    @NotNull
    @Column(name = "end_datetime")
    private LocalDateTime endDateTime;

    @NotEmpty
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder
    public Schedule(User user, CalendarGroup group, ColorPalette colorPalette, LocalDateTime startDateTime, LocalDateTime endDateTime, String title, String description) {
        this.user = user;
        this.group = group;
        this.colorPalette = colorPalette;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.title = title;
        this.description = description;
    }

    public void updateSchedule(CalendarGroup group, ColorPalette palette, LocalDateTime startDateTime, LocalDateTime endDateTime, String title, String description) {

        if (group != null) {
            this.group = group;
        }
        if (palette != null) {
            this.colorPalette = palette;
        }
        if (startDateTime != null) {
            this.startDateTime = startDateTime;
        }
        if (endDateTime != null) {
            this.endDateTime = endDateTime;
        }
        if (title != null) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
    }
}
