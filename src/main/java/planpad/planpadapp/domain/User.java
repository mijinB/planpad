package planpad.planpadapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    private String email;
    private String avatar;

    public void setUserName(String userName) {
        // validation 추가 예정

        this.userName = userName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
