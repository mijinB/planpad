package planpad.planpadapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public void updateFolderInfo(String name, String colorCode) {

        if (name != null) {
            this.name = name;
        }
        if (colorCode != null) {
            this.colorCode = colorCode;
        }
    }

    public void updateFolderOrder(Integer folderOrder) {

        if (folderOrder != null) {
            this.folderOrder = folderOrder;
        }
    }
}
