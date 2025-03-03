package planpad.planpadapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import planpad.planpadapp.dto.memo.MemoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long memoId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "memo_tag",
            joinColumns = @JoinColumn(name = "memo_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @Column(name = "memo_order")
    private int memoOrder;

    @NotEmpty
    @Column(nullable = false)
    private String title;

    private String contents;

    @Column(name = "is_fixed", nullable = false)
    private boolean isFixed;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Memo() {}

    public Memo(MemoDto dtoData) {
        this.user = dtoData.getUser();
        this.folder = dtoData.getFolder();
        this.tags = dtoData.getTags();
        this.memoOrder = dtoData.getMemoOrder();
        this.title = dtoData.getTitle();
        this.contents = dtoData.getContents();
        this.isFixed = dtoData.isFixed();
    }
}
