package planpad.planpadapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Entity
@Getter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @NotEmpty
    private String accessToken;

    @NotEmpty
    private String email;

    @Column(name = "user_name")
    private String userName;

    private String avatar;

    public void setUserName(String userName) {
        // validation 추가 예정

        this.userName = userName;
    }

    public void setEmail(String email) {
        // validation 추가 예정

        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
