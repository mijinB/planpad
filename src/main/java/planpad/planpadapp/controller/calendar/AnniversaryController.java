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
import planpad.planpadapp.dto.api.calendar.AnniversariesResponseWrapper;
import planpad.planpadapp.dto.calendar.anniversary.AnniversariesResponse;
import planpad.planpadapp.dto.calendar.anniversary.AnniversaryRequest;
import planpad.planpadapp.dto.calendar.anniversary.UpdateAnniversaryRequest;
import planpad.planpadapp.service.calendar.AnniversaryService;
import planpad.planpadapp.service.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Anniversary API", description = "기념일 관리 API")
public class AnniversaryController {

    private final UserService userService;
    private final AnniversaryService anniversaryService;

    @PostMapping("/anniversary")
    @Operation(summary = "기념일 생성", description = "새로운 기념일을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기념일 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaveResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "기념일 생성 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> createAnniversary(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid AnniversaryRequest request) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            Long anniversaryId = anniversaryService.createAnniversary(user, request);

            return ResponseEntity.ok(new SaveResponseWrapper(new SaveResponseDto(anniversaryId), "기념일 생성에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("기념일 생성에 실패하였습니다."));
        }
    }

    @GetMapping("/anniversaries")
    @Operation(summary = "전체 기념일 리스트 조회", description = "전체 기념일 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 기념일 리스트 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnniversariesResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "전체 기념일 리스트 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> getAnniversaries(@RequestHeader("Authorization") String bearerToken) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            List<AnniversariesResponse> anniversaries = anniversaryService.getAnniversaries(user);

            return ResponseEntity.ok(new AnniversariesResponseWrapper(anniversaries, "전체 기념일 리스트 조회에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("전체 기념일 리스트 조회에 실패하였습니다."));
        }
    }

    @PatchMapping("/anniversary/{id}")
    @Operation(summary = "기념일 수정", description = "기념일을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기념일 수정 성공"),
            @ApiResponse(responseCode = "400", description = "기념일 수정 실패")
    })
    public ResponseEntity<Void> updateAnniversary(@RequestHeader("Authorization") String bearerToken, @PathVariable("id") Long id, @RequestBody @Valid UpdateAnniversaryRequest request) {

        try {
            User user = userService.getUserByBearerToken(bearerToken);
            anniversaryService.updateAnniversary(user, id, request);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
