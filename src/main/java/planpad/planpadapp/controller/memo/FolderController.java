package planpad.planpadapp.controller.memo;

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
import planpad.planpadapp.dto.api.SaveResponseDto;
import planpad.planpadapp.dto.api.SaveResponseWrapper;
import planpad.planpadapp.dto.api.memo.FoldersResponseWrapper;
import planpad.planpadapp.dto.memo.FolderRequestDto;
import planpad.planpadapp.dto.memo.FolderUpdateRequestDto;
import planpad.planpadapp.dto.memo.FoldersResponseDto;
import planpad.planpadapp.service.memo.FolderService;
import planpad.planpadapp.service.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FolderController {

    private final UserService userService;
    private final FolderService folderService;

    @PostMapping("/folder")
    @Operation(summary = "폴더 생성", description = "새로운 폴더를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "폴더 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaveResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "폴더 생성 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> createFolder(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid FolderRequestDto request) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            User user = userService.getUserByUserToken(userToken);
            Long folderId = folderService.saveFolder(user, request);

            return ResponseEntity.ok(new SaveResponseWrapper(new SaveResponseDto(folderId), "폴더 생성에 성공하였습니다."));

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

    @DeleteMapping("/folder/{id}")
    @Operation(summary = "폴더 삭제", description = "폴더를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "폴더 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "폴더 삭제 실패")
    })
    public ResponseEntity<Void> deleteFolder(@RequestHeader("Authorization") String bearerToken, @PathVariable("id") Long id) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            userService.getUserByUserToken(userToken);
            folderService.deleteFolder(id);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
