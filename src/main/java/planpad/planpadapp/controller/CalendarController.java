package planpad.planpadapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.api.OnlyMessageResponseDto;
import planpad.planpadapp.dto.calendar.GroupRequestDto;
import planpad.planpadapp.dto.calendar.GroupResponseDto;
import planpad.planpadapp.service.calendar.GroupService;
import planpad.planpadapp.service.user.UserService;

@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final UserService userService;
    private final GroupService groupService;

    @PostMapping("/group")
    @Operation(summary = "그룹 생성", description = "새로운 그룹을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "그룹 생성 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> createGroup(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid GroupRequestDto request) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            User user = userService.getUserByUserToken(userToken);
            Long groupId = groupService.saveGroup(user, request);

            return ResponseEntity.ok(new GroupResponseDto(groupId));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("그룹 생성에 실패하였습니다."));
        }
    }
}
