package planpad.planpadapp.domain;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import planpad.planpadapp.converter.JsonNodeConverter;
import planpad.planpadapp.dto.memo.MemoDto;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
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

    private String contents;

    @Convert(converter = JsonNodeConverter.class)
    @Column(columnDefinition = "TEXT")
    private JsonNode tags;

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
        this.memoOrder = dtoData.getMemoOrder();
        this.title = dtoData.getTitle();
        this.contents = dtoData.getContents();
        this.tags = dtoData.getTags();
        this.isFixed = dtoData.isFixed();
    }
}
