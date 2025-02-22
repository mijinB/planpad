package planpad.planpadapp.domain;

import de.huxhorn.sulky.ulid.ULID;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import planpad.planpadapp.dto.user.SocialUserDto;

@Entity
@Getter
public class User {

    @Id
    @Column(name = "user_id")
    private String id;

    @NotNull
    @Column(name = "social_id", unique = true)
    private String socialId;

    @NotEmpty
    @Column(name = "social_type")
    private String socialType;

    @NotEmpty
    @Column(name = "access_token")
    private String accessToken;

    @NotEmpty
    @Column(unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    private String avatar;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = new ULID().nextULID();
        }
    }

    public User() {}

    public User(SocialUserDto dtoData) {
        this.socialId = dtoData.getSocialId();
        this.socialType = dtoData.getSocialType();
        this.accessToken = dtoData.getAccessToken();
        this.name = dtoData.getName();
        this.email = dtoData.getEmail();
        this.avatar = dtoData.getAvatar();
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
