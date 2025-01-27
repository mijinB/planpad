package planpad.planpadapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // user_id 1부터 시작되지 않고 51부터 시작되는 이슈 해결 : JPA 가 user_seq 테이블의 next_val(=1)을 참고하여 50개의 ID 값을 미리 할당하기 때문 -> MySQL 의 GenerationType.IDENTITY 사용으로 변경
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
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
