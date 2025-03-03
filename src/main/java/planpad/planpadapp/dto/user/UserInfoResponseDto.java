package planpad.planpadapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "사용자 정보 조회 응답 데이터")
public class UserInfoResponseDto {

    @Schema(description = "소셜 타입", example = "kakao")
    public String socialType;

    @Schema(description = "사용자 이름", example = "홍길동")
    public String name;

    @Schema(description = "사용자 이메일", example = "hong@example.com")
    public String email;

    @Schema(description = "사용자 프로필 사진 url", example = "https://example.com/avatar.jpg")
    public String avatar;
}
