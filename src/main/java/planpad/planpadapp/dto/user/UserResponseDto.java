package planpad.planpadapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "사용자 응답 데이터")
public class UserResponseDto {

    @Schema(description = "사용자 인증 토큰", example = "eyJhbGciOiJIUzI1...")
    public String token;

    @Schema(description = "사용자 이름", example = "홍길동")
    public String name;

    @Schema(description = "사용자 이메일", example = "hong@example.com")
    public String email;

    @Schema(description = "사용자 프로필 사진 url", example = "https://example.com/avatar.jpg")
    public String avatar;
}
