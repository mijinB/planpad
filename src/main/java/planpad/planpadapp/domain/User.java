package planpad.planpadapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column(name = "kakao_id", unique = true)
    private Long kakaoId;

    @NotEmpty
    @Column(name = "access_token")
    private String accessToken;

    @NotEmpty
    @Column(unique = true)
    private String email;

    @Column(name = "user_name")
    private String userName;

    private String avatar;

    public void setKakaoId(Long kakaoId) {
        this.kakaoId = kakaoId;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

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
