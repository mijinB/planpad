package planpad.planpadapp.domain.memo;

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
                @UniqueConstraint(name = "unique_user_folder", columnNames = {"user_id", "name"})
        },
        indexes = {
                @Index(name = "idx_user_folder", columnList = "user_id, name")
        }
)
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private Long folderId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotEmpty
    @Column(nullable = false)
    private String name;

    @NotEmpty
    @Column(name = "color_code", nullable = false)
    private String colorCode;

    @Column(name = "folder_order")
    private int folderOrder;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Memo> memos = new ArrayList<>();

    @Builder
    public Folder(User user, String name, String colorCode, int folderOrder) {
        this.user = user;
        this.name = name;
        this.colorCode = colorCode;
        this.folderOrder = folderOrder;
    }

    public void updateInfo(String name, String colorCode) {

        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (colorCode != null && !colorCode.isBlank()) {
            this.colorCode = colorCode;
        }
    }

    public void changeOrder(Integer folderOrder) {

        if (folderOrder != null) {
            this.folderOrder = folderOrder;
        }
    }
}
