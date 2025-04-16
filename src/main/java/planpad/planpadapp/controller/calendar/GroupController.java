package planpad.planpadapp.controller.calendar;

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
import planpad.planpadapp.dto.api.calendar.GroupsResponseWrapper;
import planpad.planpadapp.dto.calendar.GroupRequestDto;
import planpad.planpadapp.dto.calendar.GroupsResponseDto;
import planpad.planpadapp.service.calendar.GroupService;
import planpad.planpadapp.service.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Calendar Group API", description = "일정 그룹 관리 API")
public class GroupController {

    private final UserService userService;
    private final GroupService groupService;

    @PostMapping("/group")
    @Operation(summary = "그룹 생성", description = "새로운 그룹을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaveResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "그룹 생성 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> createGroup(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid GroupRequestDto request) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            Long groupId = groupService.createGroup(user, request);

            return ResponseEntity.ok(new SaveResponseWrapper(new SaveResponseDto(groupId), "그룹 생성에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("그룹 생성에 실패하였습니다."));
        }
    }

    @GetMapping("/groups")
    @Operation(summary = "그룹 리스트 조회", description = "그룹 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 리스트 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupsResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "그룹 리스트 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> getGroups(@RequestHeader("Authorization") String bearerToken) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            List<GroupsResponseDto> groups = groupService.getGroups(user);

            return ResponseEntity.ok(new GroupsResponseWrapper(groups, "그룹 리스트 조회에 성공하였습니다"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("그룹 리스트 조회에 실패하였습니다."));
        }
    }

    @PatchMapping("/group/{id}")
    @Operation(summary = "그룹 수정", description = "그룹을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 수정 성공"),
            @ApiResponse(responseCode = "400", description = "그룹 수정 실패")
    })
    public ResponseEntity<Void> updateGroup(@RequestHeader("Authorization") String bearerToken, @PathVariable("id") Long id, @RequestBody @Valid GroupRequestDto request) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            groupService.updateGroup(user, id, request);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/group/{id}")
    @Operation(summary = "그룹 삭제", description = "그룹을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 수정 성공"),
            @ApiResponse(responseCode = "400", description = "그룹 수정 실패")
    })
    public ResponseEntity<Void> deleteGroup(@RequestHeader("Authorization") String bearerToken, @PathVariable("id") Long id) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            groupService.deleteGroup(user, id);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
