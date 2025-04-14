package planpad.planpadapp.dto.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleRequestDto {

    @NotNull
    @Schema(description = "그룹 id", example = "1 (필수)")
    private Long groupId;

    @NotNull
    @Schema(description = "일정 시작 일시", example = "2025-04-14T08:10:25 (필수)")
    private LocalDateTime startDateTime;

    @NotNull
    @Schema(description = "일정 종료 일시", example = "2025-04-14T22:15:30 (필수)")
    private LocalDateTime endDateTime;

    @NotEmpty
    @Schema(description = "일정 제목", example = "동물병원 상담 (필수)")
    private String title;

    @Schema(description = "일정 설명", example = "동물병원 업무시간 : AM09:00 ~ PM06:00")
    private String description;

    @NotEmpty
    @Schema(description = "색상 코드", example = "#000000 (필수)")
    private String colorCode;
}
