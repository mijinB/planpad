package planpad.planpadapp.dto.user.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InfoKakaoAccount {

    @JsonProperty("profile_nickname_needs_agreement")
    public String profileNicknameNeedsAgreement;

    @JsonProperty("profile_image_needs_agreement")
    public String profileImageNeedsAgreement;

    @JsonProperty("has_email")
    public String hasEmail;

    @JsonProperty("email_needs_agreement")
    public String emailNeedsAgreement;

    @JsonProperty("is_email_valid")
    public String isEmailValid;

    @JsonProperty("is_email_verified")
    public String isEmailVerified;

    public String email;
    public InfoProfile profile;
}
