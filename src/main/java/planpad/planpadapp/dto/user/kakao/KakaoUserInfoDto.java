package planpad.planpadapp.dto.user.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoUserInfoDto {

    public Long id;

    @JsonProperty("connected_at")
    public String connectedAt;

    public Properties properties;

    @JsonProperty("kakao_account")
    public KakaoAccount kakaoAccount;
}
