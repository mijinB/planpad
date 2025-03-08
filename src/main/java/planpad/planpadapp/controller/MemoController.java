package planpad.planpadapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.api.OnlyMessageResponseDto;
import planpad.planpadapp.dto.api.memo.MemosResponseWrapper;
import planpad.planpadapp.dto.memo.FolderDto;
import planpad.planpadapp.provider.JwtTokenProvider;
import planpad.planpadapp.service.memo.FolderService;
import planpad.planpadapp.service.user.UserService;

@RestController
@RequiredArgsConstructor
public class MemoController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final FolderService folderService;

    @PostMapping("/folder")
    @Operation(summary = "폴더 생성", description = "새로운 폴더를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "폴더 생성 성공"),
            @ApiResponse(responseCode = "400", description = "폴더 생성 실패")
    })
    public ResponseEntity<Void> createFolder(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid FolderDto request) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            String userId = jwtTokenProvider.getUserIdFromToken(userToken);
            User user = userService.getUserById(userId);

            // 테스트를 위해 default 0으로 setting
            if (request.getFolderOrder() == null) {
                request.setFolderOrder(1);
            }
            folderService.saveFolder(user, request);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


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
