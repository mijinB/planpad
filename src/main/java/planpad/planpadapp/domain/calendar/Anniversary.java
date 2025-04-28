package planpad.planpadapp.domain.calendar;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.enums.AnniversaryRecurrenceType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Anniversary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anniversary_id")
    private Long anniversaryId;

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

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_type")
    private AnniversaryRecurrenceType recurrenceType;

    @NotEmpty
    private String title;

    @Builder
    public Anniversary(User user, CalendarGroup group, ColorPalette colorPalette, LocalDate startDate, LocalDate endDate, AnniversaryRecurrenceType recurrenceType, String title) {
        this.user = user;
        this.group = group;
        this.colorPalette = colorPalette;
        this.startDate = startDate;
        this.endDate = endDate;
        this.recurrenceType = recurrenceType;
        this.title = title;
    }

    public void updateAnniversary(CalendarGroup group, ColorPalette palette, LocalDate startDate, LocalDate endDate, AnniversaryRecurrenceType recurrenceType, String title) {

        if (group != null) {
            this.group = group;
        }
        if (palette != null) {
            this.colorPalette = palette;
        }
        if (startDate != null) {
            this.startDate = startDate;
        }
        if (endDate != null) {
            this.endDate = endDate;
        }
        if (recurrenceType != null) {
            this.recurrenceType = recurrenceType;
        }
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
    }

    public Anniversary copyWithNewStartDate(LocalDate newStartDate) {
        long daysBetween = ChronoUnit.DAYS.between(this.startDate, newStartDate);

        return Anniversary.builder()
                .user(this.user)
                .group(this.group)
                .colorPalette(this.colorPalette)
                .startDate(newStartDate)
                .endDate(newStartDate.plusDays(daysBetween))
                .recurrenceType(this.recurrenceType)
                .title(this.title)
                .build();
    }
}
