package planpad.planpadapp.dto.calendar.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateScheduleRequest {

    @Schema(description = "그룹 id", example = "1")
    private Long groupId;

    @Schema(description = "색상 팔레트 id", example = "#000000")
    private Long paletteId;

    @Schema(description = "일정 시작 일시", example = "DateTime 타입")
    private LocalDateTime startDateTime;

    @Schema(description = "일정 종료 일시", example = "DateTime 타입")
    private LocalDateTime endDateTime;

    @Schema(description = "일정 반복 주기 정보")
    private ScheduleRecurrenceDto recurrence;

    @Schema(description = "일정 제목", example = "동물병원 상담")
    private String title;

    @Schema(description = "일정 설명", example = "동물병원 업무시간 : AM09:00 ~ PM06:00")
    private String description;
}
