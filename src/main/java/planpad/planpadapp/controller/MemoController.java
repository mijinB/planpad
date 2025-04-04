package planpad.planpadapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemoController {

    private final UserService userService;
    private final FolderService folderService;
    private final MemoService memoService;

    @PostMapping("/folder")
    @Operation(summary = "폴더 생성", description = "새로운 폴더를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "폴더 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FolderSaveResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "폴더 생성 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> createFolder(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid FolderRequestDto request, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errorMessages);
        }

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            User user = userService.getUserByUserToken(userToken);
            Long folderId = folderService.saveFolder(user, request);

            return ResponseEntity.ok(new FolderSaveResponseWrapper(new FolderResponseDto(folderId), "폴더 생성에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("폴더 생성에 실패하였습니다."));
        }
    }

    @GetMapping("/folders")
    @Operation(summary = "폴더 리스트 조회", description = "폴더 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "폴더 리스트 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoldersResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "폴더 리스트 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> getFolders(@RequestHeader("Authorization") String bearerToken) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            User user = userService.getUserByUserToken(userToken);
            List<FoldersResponseDto> folders = folderService.getFolders(user);

            return ResponseEntity.ok(new FoldersResponseWrapper(folders, "폴더 리스트 조회에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("폴더 리스트 조회에 실패하였습니다."));
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
            userService.getUserByUserToken(userToken);
            folderService.updateFolder(id, request);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/memo")
    @Operation(summary = "메모 생성", description = "새로운 메모를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메모 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemoResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "메모 생성 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> createMemo(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid MemoRequestDto request, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errorMessages);
        }

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            User user = userService.getUserByUserToken(userToken);
            Long memoId = memoService.saveMemo(user, request);

            return ResponseEntity.ok(new MemoResponseWrapper(new MemoResponseDto(memoId), "메모 생성에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("메모 생성에 실패하였습니다."));
        }
    }

    @GetMapping("/folder/{folderId}/memos")
    @Operation(summary = "특정 폴더 내 메모 리스트 조회", description = "특정 폴더 내 메모 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 폴더 내 메모 리스트 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemosResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "특정 폴더 내 메모 리스트 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> getMemosByFolder(@RequestHeader("Authorization") String bearerToken, @PathVariable("folderId") Long folderId) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            userService.getUserByUserToken(userToken);
            List<MemosResponseDto> memos = memoService.getMemosByFolder(folderId);

            return ResponseEntity.ok(new MemosResponseWrapper(memos, "특정 폴더 내 메모 리스트 조회에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("특정 폴더 내 메모 리스트 조회에 실패하였습니다."));
        }
    }

    @GetMapping("/memos")
    @Operation(summary = "전체 메모 리스트 조회", description = "전체 메모 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 메모 리스트 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemosResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "전체 메모 리스트 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> getMemos(@RequestHeader("Authorization") String bearerToken) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            User user = userService.getUserByUserToken(userToken);
            List<MemosResponseDto> memos = memoService.getMemos(user);

            return ResponseEntity.ok(new MemosResponseWrapper(memos, "전체 메모 리스트 조회에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("전체 메모 리스트 조회에 실패하였습니다."));
        }
    }

    @PatchMapping("/memo/{id}")
    @Operation(summary = "메모 수정", description = "메모 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메모 수정 성공"),
            @ApiResponse(responseCode = "400", description = "메모 수정 실패")
    })
    public ResponseEntity<Void> updateMemo(@RequestHeader("Authorization") String bearerToken, @PathVariable("id") Long id, @RequestBody @Valid MemoUpdateRequestDto request) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            userService.getUserByUserToken(userToken);
            memoService.updateMemo(id, request);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/memo/{id}")
    @Operation(summary = "메모 삭제", description = "메모를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메모 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "메모 삭제 실패")
    })
    public ResponseEntity<Void> deleteMemo(@RequestHeader("Authorization") String bearerToken, @PathVariable("id") Long id) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            userService.getUserByUserToken(userToken);
            memoService.deleteMemo(id);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
