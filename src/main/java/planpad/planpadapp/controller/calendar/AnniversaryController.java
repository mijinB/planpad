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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.api.OnlyMessageResponseDto;
import planpad.planpadapp.dto.api.SaveResponseDto;
import planpad.planpadapp.dto.api.SaveResponseWrapper;
import planpad.planpadapp.dto.calendar.anniversary.AnniversaryRequest;
import planpad.planpadapp.service.calendar.AnniversaryService;
import planpad.planpadapp.service.user.UserService;

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
}
