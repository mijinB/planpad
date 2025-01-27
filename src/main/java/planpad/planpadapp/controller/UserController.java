package planpad.planpadapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.common.ErrorResponseDto;
import planpad.planpadapp.dto.common.OkResponseWrapper;
import planpad.planpadapp.dto.user.UserResponseDto;
import planpad.planpadapp.dto.user.kakao.KakaoUserRequestDto;
import planpad.planpadapp.provider.JwtTokenProvider;
import planpad.planpadapp.service.user.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/users")
    @Operation(summary = "소셜 로그인", description = "사용자의 최초 연결 여부에 따라 회원가입/로그인이 진행됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소셜 회원가입/로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OkResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "소셜 회원가입/로그인 실패 = 잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<Object> socialLogIn(@RequestBody @Valid KakaoUserRequestDto request) {

        try {
            User user = userService.kakaoLoginOrJoin(request.getCode());
            String userToken = jwtTokenProvider.createToken(user.getId().toString());

            UserResponseDto userData = new UserResponseDto();
            userData.setToken(userToken);
            userData.setName(user.getUserName());
            userData.setEmail(user.getEmail());
            userData.setAvatar(user.getAvatar());

            OkResponseWrapper apiResponseWrapper = new OkResponseWrapper();
            apiResponseWrapper.setData(userData);
            apiResponseWrapper.setMessage("소셜 회원가입/로그인에 성공하였습니다.");

            log.info("카카오톡 로그인 성공 userData = {}", userData);
            return ResponseEntity.ok(apiResponseWrapper);

        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto();
            errorResponseDto.setMessage("소셜 회원가입/로그인에 실패하였습니다.");

            log.info("카카오톡 로그인 실패 socialLogIn exception = {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
        }
    }

    @PostMapping("/kakao-unlink")
    @Operation(summary = "카카오톡 연결 끊기", description = "(회원탈퇴 개념) 카카오톡 소셜 로그인 연결을 끊습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카카오톡 연결 끊기 성공"),
            @ApiResponse(responseCode = "400", description = "카카오톡 연결 끊기 실패 = 잘못된 요청")
    })
    public ResponseEntity<Void> socialUnLink(@RequestHeader("Authorization") String bearerToken) {

        try {
            userService.kakaoUnLink(bearerToken);

            log.info("카카오톡 연결 끊기 성공");
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.info("카카오톡 연결 끊기 실패 socialUnLink exception = {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}

/**
 * 6. log.info 를 debug 로 바꾸기 (debug 로 하면 console 에 안찍히던데 이유 찾아내기)
 * 7. naver, google 소셜 로그인 기능 진행하면서 판단에 따라 API url, controller(위 api) 메서드명 수정
 * 8. repository 에 있는 것만이라도 테스트코드 작성하기
 */
