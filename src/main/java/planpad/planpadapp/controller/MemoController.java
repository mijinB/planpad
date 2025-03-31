package planpad.planpadapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.api.OnlyMessageResponseDto;
import planpad.planpadapp.dto.api.memo.FolderSaveResponseWrapper;
import planpad.planpadapp.dto.api.memo.FoldersResponseWrapper;
import planpad.planpadapp.dto.api.memo.MemoResponseWrapper;
import planpad.planpadapp.dto.api.memo.MemosResponseWrapper;
import planpad.planpadapp.dto.memo.*;
import planpad.planpadapp.service.memo.FolderService;
import planpad.planpadapp.service.memo.MemoService;
import planpad.planpadapp.service.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemoController {

    private final UserService userService;
    private final FolderService folderService;
    private final MemoService memoService;

    @GetMapping("/folders")
    @Operation(summary = "폴더 리스트 조회", description = "폴더 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "폴더 리스트 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoldersResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "폴더 리스트 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> getFolders(@RequestHeader("Authorization") String bearerToken) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            User user = userService.getUserByBearerToken(userToken);
            List<FoldersResponseDto> folders = folderService.getFolders(user);

            return ResponseEntity.ok(new FoldersResponseWrapper(folders, "폴더 리스트 조회에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OnlyMessageResponseDto("폴더 리스트 조회에 실패하였습니다."));
        }
    }

    @PostMapping("/folder")
    @Operation(summary = "폴더 생성", description = "새로운 폴더를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "폴더 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FolderSaveResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "폴더 생성 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> createFolder(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid FolderRequestDto request) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            User user = userService.getUserByBearerToken(userToken);
            Long folderId = folderService.saveFolder(user, request);

            return ResponseEntity.ok(new FolderSaveResponseWrapper(new FolderResponseDto(folderId), "폴더 생성에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OnlyMessageResponseDto("폴더 생성에 실패하였습니다."));
        }
    }

    @PatchMapping("/folder/{id}")
    @Operation(summary = "폴더 수정", description = "폴더 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "폴더 수정 성공"),
            @ApiResponse(responseCode = "400", description = "폴더 수정 실패")
    })
    public ResponseEntity<Void> updateFolder(@RequestHeader("Authorization") String bearerToken, @PathVariable("id") Long id, @RequestBody @Valid FolderUpdateRequestDto request) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            userService.getUserByBearerToken(userToken);
            folderService.updateFolder(id, request);

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
        User user = userService.getUserByBearerToken(userToken);
    }

    @PostMapping("/memo")
    @Operation(summary = "메모 생성", description = "새로운 메모를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메모 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemoResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "메모 생성 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> createMemo(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid MemoRequestDto request) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            User user = userService.getUserByBearerToken(userToken);
            Long memoId = memoService.saveMemo(user, request);

            return ResponseEntity.ok(new MemoResponseWrapper(new MemoResponseDto(memoId), "메모 생성에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OnlyMessageResponseDto("메모 생성에 실패하였습니다."));
        }
    }
}
