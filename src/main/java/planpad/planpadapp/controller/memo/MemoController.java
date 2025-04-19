package planpad.planpadapp.controller.memo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.api.OnlyMessageResponseDto;
import planpad.planpadapp.dto.api.SaveResponseDto;
import planpad.planpadapp.dto.api.SaveResponseWrapper;
import planpad.planpadapp.dto.api.memo.MemoResponseWrapper;
import planpad.planpadapp.dto.api.memo.MemosResponseWrapper;
import planpad.planpadapp.dto.memo.*;
import planpad.planpadapp.service.memo.MemoService;
import planpad.planpadapp.service.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Memo API", description = "메모 관리 API")
public class MemoController {

    private final UserService userService;
    private final MemoService memoService;

    @PostMapping("/memo")
    @Operation(summary = "메모 생성", description = "새로운 메모를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메모 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaveResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "메모 생성 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> createMemo(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid MemoRequest request) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            Long memoId = memoService.createMemo(user, request);

            return ResponseEntity.ok(new SaveResponseWrapper(new SaveResponseDto(memoId), "메모 생성에 성공하였습니다."));

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
    public ResponseEntity<Object> getMemosInFolder(@RequestHeader("Authorization") String bearerToken, @PathVariable("folderId") Long folderId) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            List<MemosResponse> memos = memoService.getMemosInFolder(user, folderId);

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
            User user = userService.getUserByBearerToken(bearerToken);
            List<MemosResponse> memos = memoService.getMemosByUser(user);

            return ResponseEntity.ok(new MemosResponseWrapper(memos, "전체 메모 리스트 조회에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("전체 메모 리스트 조회에 실패하였습니다."));
        }
    }

    @GetMapping("/memo/{id}")
    @Operation(summary = "메모 조회", description = "특정 메모를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메모 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemoResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "메모 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> getMemo(@RequestHeader("Authorization") String bearerToken, @PathVariable("id") Long id) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            MemoResponse memo = memoService.getMemo(user, id);

            return ResponseEntity.ok(new MemoResponseWrapper(memo, "메모 조회에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("메모 조회에 실패하였습니다."));
        }
    }

    @PatchMapping("/memo/{id}")
    @Operation(summary = "메모 수정", description = "메모 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메모 수정 성공"),
            @ApiResponse(responseCode = "400", description = "메모 수정 실패")
    })
    public ResponseEntity<Void> updateMemo(@RequestHeader("Authorization") String bearerToken, @PathVariable("id") Long id, @RequestBody @Valid UpdateMemoRequest request) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            memoService.updateMemo(user, id, request);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/memos")
    @Operation(summary = "메모 일괄 이동", description = "선택한 메모를 일괄 이동합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메모 일괄 이동 성공"),
            @ApiResponse(responseCode = "400", description = "메모 일괄 이동 실패")
    })
    public ResponseEntity<Void> moveMemos(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid MoveMemosRequest request) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            memoService.moveMemosToFolder(user, request.getFolderId(), request.getMemoIds());

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
            User user = userService.getUserByBearerToken(bearerToken);
            memoService.deleteMemo(user, id);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/memos")
    @Operation(summary = "메모 일괄 삭제", description = "선택한 메모를 일괄 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메모 일괄 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "메모 일괄 삭제 실패")
    })
    public ResponseEntity<Void> deleteMemos(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid DeleteMemosRequest request) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            memoService.deleteMemos(user, request.getIds());

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
