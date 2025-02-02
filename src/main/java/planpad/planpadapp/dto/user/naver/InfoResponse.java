package planpad.planpadapp.dto.user.naver;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InfoResponse {
    
    public String id;

    @JsonProperty("profile_image")
    public String profileImage;

    public String gender;
    public String email;
    public String name;
    public String birthday;
}
