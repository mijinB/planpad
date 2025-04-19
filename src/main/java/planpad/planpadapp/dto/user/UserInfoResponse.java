package planpad.planpadapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    @Schema(description = "소셜 타입", example = "kakao")
    private String socialType;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(description = "사용자 이메일", example = "hong@example.com")
    private String email;

    @Schema(description = "사용자 프로필 사진 url", example = "https://example.com/avatar.jpg")
    private String avatar;
}
