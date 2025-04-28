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
public class SchedulesResponse {

    @Schema(description = "색상 코드", example = "#FFFFFF")
    private String colorCode;

    @Schema(description = "일정/기념일 시작 일자", example = "Date 타입")
    private LocalDate startDate;

    @Schema(description = "일정 시작 시간", example = "Time 타입")
    private LocalTime startTime;

    @Schema(description = "일정/기념일 종료 일자", example = "Date 타입")
    private LocalDate endDate;

    @Schema(description = "일정 종료 시간", example = "Time 타입")
    private LocalTime endTime;

    @Schema(description = "일정 제목", example = "동물병원 상담")
    private String title;
}
