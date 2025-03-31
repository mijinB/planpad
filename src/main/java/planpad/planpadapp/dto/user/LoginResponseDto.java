package planpad.planpadapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "로그인 응답 데이터")
public class LoginResponseDto {

    @Schema(description = "사용자 인증 토큰", example = "eyJhbGciOiJIUzI1...")
    public String token;

    public LoginResponseDto(String token) {
        this.token = token;
    }
}
