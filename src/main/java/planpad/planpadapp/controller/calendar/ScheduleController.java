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
import planpad.planpadapp.dto.api.calendar.DaySchedulesResponseWrapper;
import planpad.planpadapp.dto.api.calendar.ScheduleResponseWrapper;
import planpad.planpadapp.dto.api.calendar.SchedulesResponseWrapper;
import planpad.planpadapp.dto.calendar.schedule.*;
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
    public ResponseEntity<Object> createSchedule(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid ScheduleRequest request) {

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
    public ResponseEntity<Object> getSchedulesByMonth(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid MonthSchedulesRequest request) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            Map<Integer, List<SchedulesResponse>> monthSchedules = scheduleService.getSchedulesByMonth(user, request);

            return ResponseEntity.ok(new SchedulesResponseWrapper(monthSchedules, "월별 일정 조회에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("월별 일정 조회에 실패하였습니다."));
        }
    }

    @GetMapping("/schedules/week")
    @Operation(summary = "주별 일정 조회", description = "주별 일정을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주별 일정 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchedulesResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "주별 일정 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> getSchedulesByWeek(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid WeekSchedulesRequest request) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            Map<Integer, List<SchedulesResponse>> weekSchedules = scheduleService.getSchedulesByWeek(user, request);

            return ResponseEntity.ok(new SchedulesResponseWrapper(weekSchedules, "주별 일정 조회에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("주별 일정 조회에 실패하였습니다."));
        }
    }

    @GetMapping("/schedules/day")
    @Operation(summary = "일별 일정 조회", description = "일별 일정을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일별 일정 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DaySchedulesResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "일별 일정 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> getSchedulesByDay(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid DaySchedulesRequest request) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            List<SchedulesResponse> daySchedules = scheduleService.getSchedulesByDay(user, request);

            return ResponseEntity.ok(new DaySchedulesResponseWrapper(daySchedules, "일별 일정 조회에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("일별 일정 조회에 실패하였습니다."));
        }
    }

    @GetMapping("/schedule/{id}")
    @Operation(summary = "일정 상세 조회", description = "일정 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일정 상세 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "일정 상세 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> getSchedule(@RequestHeader("Authorization") String bearerToken, @PathVariable("id") Long id) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            ScheduleResponse schedule = scheduleService.getSchedule(user, id);

            return ResponseEntity.ok(new ScheduleResponseWrapper(schedule, "일정 상세 조회에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("일정 상세 조회에 실패하였습니다."));
        }
    }

    @PatchMapping("/schedule/{id}")
    @Operation(summary = "일정 수정", description = "일정을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일정 수정 성공"),
            @ApiResponse(responseCode = "400", description = "일정 수정 실패")
    })
    public ResponseEntity<Void> updateSchedule(@RequestHeader("Authorization") String bearerToken, @PathVariable("id") Long id, @RequestBody @Valid UpdateScheduleRequest request) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            scheduleService.updateSchedule(user, id, request);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/schedule/{id}")
    @Operation(summary = "일정 삭제", description = "일정을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일정 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "일정 삭제 실패")
    })
    public ResponseEntity<Void> deleteSchedule(@RequestHeader("Authorization") String bearerToken, @PathVariable("id") Long id) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            scheduleService.deleteSchedule(user, id);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
