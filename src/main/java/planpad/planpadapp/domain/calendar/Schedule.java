package planpad.planpadapp.domain.calendar;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
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
    @Column(name = "start_datetime")
    private LocalDateTime startDateTime;

    @NotNull
    @Column(name = "end_datetime")
    private LocalDateTime endDateTime;

    @NotEmpty
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotEmpty
    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "color_name")
    private String colorName;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;
}
