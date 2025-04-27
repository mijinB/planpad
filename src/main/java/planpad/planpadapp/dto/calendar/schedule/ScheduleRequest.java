package planpad.planpadapp.dto.calendar.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ScheduleRequest {

    @NotNull
    @Schema(description = "그룹 id", example = "1 (필수)")
    private Long groupId;

    @NotNull
    @Schema(description = "색상 팔레트 id", example = "1 (필수)")
    private Long paletteId;

    @NotNull
    @Schema(description = "일정 시작 일자", example = "Date 타입 (필수)")
    private LocalDate startDate;

    @Schema(description = "일정 시작 시간", example = "Time 타입")
    private LocalTime startTime;

    @Schema(description = "일정 종료 일자", example = "Date 타입")
    private LocalDate endDate;

    @Schema(description = "일정 종료 시간", example = "Time 타입")
    private LocalTime endTime;

    @Schema(description = "일정 반복 주기 정보")
    private ScheduleRecurrenceDto recurrence;

    @NotEmpty
    @Schema(description = "일정 제목", example = "동물병원 상담 (필수)")
    private String title;

    @Schema(description = "일정 설명", example = "동물병원 업무시간 : AM09:00 ~ PM06:00")
    private String description;
}
