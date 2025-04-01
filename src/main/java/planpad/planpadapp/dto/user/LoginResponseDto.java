package planpad.planpadapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 응답 데이터")
public class LoginResponseDto {

    @Schema(description = "사용자 인증 토큰", example = "eyJhbGciOiJIUzI1...")
    private String token;
}
