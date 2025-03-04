package planpad.planpadapp.domain;

import de.huxhorn.sulky.ulid.ULID;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import planpad.planpadapp.dto.user.UserDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    @NotNull
    @Column(name = "social_id", unique = true, nullable = false)
    private String socialId;

    @NotEmpty
    @Column(name = "social_type", nullable = false)
    private String socialType;

    @NotEmpty
    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @NotEmpty
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "name")
    private String name;

    private String avatar;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> folders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Memo> memos = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();


    @PrePersist
    public void generateId() {
        if (this.userId == null) {
            this.userId = new ULID().nextULID();
        }
    }

    public User() {}

    public User(UserDto dtoData) {
        this.socialId = dtoData.getSocialId();
        this.socialType = dtoData.getSocialType();
        this.accessToken = dtoData.getAccessToken();
        this.name = dtoData.getName();
        this.email = dtoData.getEmail();
        this.avatar = dtoData.getAvatar();
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
