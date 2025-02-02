package planpad.planpadapp.dto.user.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InfoProperties {

    public String nickname;

    @JsonProperty("profile_image")
    public String profileImage;

    @JsonProperty("thumbnail_image")
    public String thumbnailImage;
}
