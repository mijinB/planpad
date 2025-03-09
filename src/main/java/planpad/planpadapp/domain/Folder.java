package planpad.planpadapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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
    private String name;

    @NotEmpty
    @Column(name = "color_code", nullable = false)
    private String colorCode;

    @Column(name = "folder_order")
    private int folderOrder;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Memo> memos = new ArrayList<>();


    public Folder() {}

    public Folder(User user, String name, int folderOrder, String colorCode) {
        this.user = user;
        this.name = name;
        this.folderOrder = folderOrder;
        this.colorCode = colorCode;
    }
}
