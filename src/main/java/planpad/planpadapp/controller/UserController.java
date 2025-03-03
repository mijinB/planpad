package planpad.planpadapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.api.user.LoginResponseWrapper;
import planpad.planpadapp.dto.api.OnlyMessageResponseDto;
import planpad.planpadapp.dto.api.user.UserResponseWrapper;
import planpad.planpadapp.dto.user.LoginRequestDto;
import planpad.planpadapp.dto.user.LoginResponseDto;
import planpad.planpadapp.dto.user.SocialUnLinkRequestDto;
import planpad.planpadapp.dto.user.UserInfoResponseDto;
import planpad.planpadapp.provider.JwtTokenProvider;
import planpad.planpadapp.service.user.UserService;

@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    @Operation(summary = "소셜 로그인", description = "사용자의 최초 연결 여부에 따라 회원가입/로그인이 진행됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소셜 회원가입/로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "소셜 회원가입/로그인 실패 = 잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> socialLogin(@RequestBody @Valid LoginRequestDto request) {

        String socialType = request.getSocialType();
        String code = request.getCode();

        try {
            if (!"kakao".equalsIgnoreCase(socialType) && !"naver".equalsIgnoreCase(socialType) && !"google".equalsIgnoreCase(socialType)) {
                throw new IllegalArgumentException("지원하지 않는 소셜 타입");
            }

            User user = userService.socialLoginOrJoin(socialType, code);
            String userToken = jwtTokenProvider.createToken(user.getUserId());

            LoginResponseDto loginUserData = new LoginResponseDto();
            loginUserData.setToken(userToken);

            LoginResponseWrapper loginResponse = new LoginResponseWrapper();
            loginResponse.setData(loginUserData);
            loginResponse.setMessage("소셜 회원가입/로그인에 성공하였습니다.");

            return ResponseEntity.ok(loginResponse);

        } catch (Exception e) {
            OnlyMessageResponseDto onlyMessageResponse = new OnlyMessageResponseDto();
            onlyMessageResponse.setMessage("소셜 회원가입/로그인에 실패하였습니다.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(onlyMessageResponse);
        }
    }

    @DeleteMapping("/unlink")
    @Operation(summary = "소셜 로그인 연결 끊기", description = "(회원탈퇴 개념) 소셜 로그인 연결을 끊습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소셜 로그인 연결 끊기 성공"),
            @ApiResponse(responseCode = "400", description = "소셜 로그인 연결 끊기 실패 = 잘못된 요청")
    })
    public ResponseEntity<Void> socialUnLink(@RequestBody @Valid SocialUnLinkRequestDto request, @RequestHeader("Authorization") String bearerToken) {

        String socialType = request.getSocialType();

        try {
            if (!"kakao".equalsIgnoreCase(socialType) && !"naver".equalsIgnoreCase(socialType) && !"google".equalsIgnoreCase(socialType)) {
                throw new IllegalArgumentException("지원하지 않는 소셜 타입");
            }

            userService.socialUnLink(socialType, bearerToken);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/me")
    @Operation(summary = "사용자 정보 조회", description = "사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "사용자 정보 조회 실패 = 잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> userInfo(@RequestHeader("Authorization") String bearerToken) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");

            User user = userService.getUserByBearerToken(userToken);

            UserInfoResponseDto userData = new UserInfoResponseDto();
            userData.setSocialType(user.getSocialType());
            userData.setName(user.getName());
            userData.setEmail(user.getEmail());
            userData.setAvatar(user.getAvatar());

            UserResponseWrapper userResponseWrapper = new UserResponseWrapper();
            userResponseWrapper.setData(userData);
            userResponseWrapper.setMessage("사용자 정보 조회를 성공하였습니다.");

            return ResponseEntity.ok(userResponseWrapper);

        } catch (Exception e) {
            OnlyMessageResponseDto onlyMessageResponse = new OnlyMessageResponseDto();
            onlyMessageResponse.setMessage("사용자 정보 조회를 실패하였습니다.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(onlyMessageResponse);
        }
    }
}
