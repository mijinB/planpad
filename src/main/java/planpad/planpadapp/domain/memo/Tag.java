package planpad.planpadapp.domain.memo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.domain.User;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_user_tag", columnNames = {"user_id", "name"})
        },
        indexes = {
                @Index(name = "idx_user_tag", columnList = "user_id, name")
        }
)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotEmpty
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Memo> memos = new HashSet<>();

    @Builder
    public Tag(User user, String name) {
        this.user = user;
        this.name = name;
    }
}
