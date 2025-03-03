package planpad.planpadapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.api.OnlyMessageResponseDto;
import planpad.planpadapp.dto.api.memo.MemosResponseWrapper;
import planpad.planpadapp.provider.JwtTokenProvider;
import planpad.planpadapp.service.user.UserService;

@RestController
@RequiredArgsConstructor
public class MemoController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @GetMapping("/memos")
    @Operation(summary = "메모 리스트 조회", description = "메모 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메모 리스트 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemosResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "메모 리스트 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public void getMemos(@RequestHeader("Authorization") String bearerToken) {

        String userToken = bearerToken.replace("Bearer ", "");
        String userId = jwtTokenProvider.getUserIdFromToken(userToken);
        User user = userService.getUserById(userId);
    }
}
