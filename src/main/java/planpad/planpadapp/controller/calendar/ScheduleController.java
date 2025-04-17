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
import planpad.planpadapp.dto.api.calendar.SchedulesResponseWrapper;
import planpad.planpadapp.dto.calendar.MonthScheduleResponseDto;
import planpad.planpadapp.dto.calendar.MonthSchedulesRequestDto;
import planpad.planpadapp.dto.calendar.ScheduleRequestDto;
import planpad.planpadapp.service.calendar.ScheduleService;
import planpad.planpadapp.service.user.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Schedule API", description = "일정 관리 API")
public class ScheduleController {

    private final UserService userService;
    private final ScheduleService scheduleService;

    @PostMapping("/schedule")
    @Operation(summary = "일정 생성", description = "새로운 일정을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일정 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaveResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "일정 생성 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> createSchedule(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid ScheduleRequestDto request) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            Long scheduleId = scheduleService.createSchedule(user, request);

            return ResponseEntity.ok(new SaveResponseWrapper(new SaveResponseDto(scheduleId), "일정 생성에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("일정 생성에 실패하였습니다."));
        }
    }

    @GetMapping("/schedules/month")
    @Operation(summary = "월별 일정 조회", description = "월별 일정을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "월별 일정 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchedulesResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "월별 일정 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> getMonthSchedules(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid MonthSchedulesRequestDto request) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            Map<Integer, List<Object>> monthSchedule = scheduleService.getSchedulesByMonth(user, request);

            return ResponseEntity.ok(new SchedulesResponseWrapper(monthSchedule, "월별 일정 조회에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("월별 일정 조회에 실패하였습니다."));
        }
    }
}
