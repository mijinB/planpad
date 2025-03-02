package planpad.planpadapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import planpad.planpadapp.dto.memo.FolderDto;

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

    @Column(name = "folder_order")
    private int folderOrder;

    @Column(name = "color_code")
    private String colorCode;

    public Folder() {}

    public Folder(FolderDto dtoData) {
        this.user = dtoData.getUser();
        this.folderOrder = dtoData.getFolderOrder();
        this.colorCode = dtoData.getColorCode();
    }
}
