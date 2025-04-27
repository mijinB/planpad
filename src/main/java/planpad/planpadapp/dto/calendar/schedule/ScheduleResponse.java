package planpad.planpadapp.dto.calendar.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {

    @Schema(description = "그룹 id", example = "1")
    private Long groupId;

    @Schema(description = "색상 팔레트 id", example = "1")
    private Long paletteId;

    @Schema(description = "일정 시작 일자", example = "Date 타입")
    private LocalDate startDate;

    @Schema(description = "일정 시작 시간", example = "Time 타입")
    private LocalTime startTime;

    @Schema(description = "일정 종료 일자", example = "Date 타입")
    private LocalDate endDate;

    @Schema(description = "일정 종료 시간", example = "Time 타입")
    private LocalTime endTime;

    @Schema(description = "일정 반복 주기 정보")
    private ScheduleRecurrenceDto recurrence;

    @Schema(description = "일정 제목", example = "동물병원 상담")
    private String title;

    @Schema(description = "일정 설명", example = "동물병원 업무시간 : AM09:00 ~ PM06:00")
    private String description;
}
