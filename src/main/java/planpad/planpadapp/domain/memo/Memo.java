package planpad.planpadapp.domain.memo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import planpad.planpadapp.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "memo_order")
    private int memoOrder;

    @NotEmpty
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_fixed")
    private Boolean isFixed;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "memo_tag",
            joinColumns = @JoinColumn(name = "memo_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @Builder
    public Memo(User user, Folder folder, int memoOrder, String title, String content, Boolean isFixed) {
        this.user = user;
        this.folder = folder;
        this.memoOrder = memoOrder;
        this.title = title;
        this.content = content;
        this.isFixed = isFixed;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void updateInfo(Folder folder, String title, String content, Boolean fixed) {

        if (folder != null) {
            this.folder = folder;
        }
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
        if (fixed != null) {
            this.isFixed = fixed;
        }
    }

    public void changeOrder(Integer memoOrder) {

        if (memoOrder != null) {
            this.memoOrder = memoOrder;
        }
    }

    public void moveToFolder(Folder folder) {
        this.folder = folder;
    }

    public void clearTags() {
        for (Tag tag : new ArrayList<>(tags)) {
            removeTag(tag);
        }
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getMemos().remove(this);
    }
}
