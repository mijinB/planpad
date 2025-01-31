package planpad.planpadapp.dto.user.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Profile {

    public String nickname;

    @JsonProperty("thumbnail_image_url")
    public String thumbnailImageUrl;

    @JsonProperty("profile_image_url")
    public String profileImageUrl;

    @JsonProperty("is_default_image")
    public String isDefaultImage;

    @JsonProperty("is_default_nickname")
    public String isDefaultNickname;
}
