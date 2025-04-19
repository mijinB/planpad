package planpad.planpadapp.domain.calendar;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.domain.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_user_palette", columnNames = {"user_id", "color_code"})
        },
        indexes = {
                @Index(name = "idx_user_palette", columnList = "user_id, color_code")
        }
)
public class ColorPalette {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "color_id")
    private Long colorId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotEmpty
    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "color_name")
    private String colorName;

    @OneToMany(mappedBy = "colorPalette", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "colorPalette", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Anniversary> anniversaries = new ArrayList<>();

    @Builder
    public ColorPalette(User user, String colorCode, String colorName) {
        this.user = user;
        this.colorCode = colorCode.toUpperCase();
        this.colorName = colorName;
    }

    public void updateColor(String colorCode, String colorName) {

        if (colorCode != null) {
            this.colorCode = colorCode;
        }
        if (colorName != null) {
            this.colorName = colorName;
        }
    }
}
