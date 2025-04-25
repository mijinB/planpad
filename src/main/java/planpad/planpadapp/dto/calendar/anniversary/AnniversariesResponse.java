package planpad.planpadapp.dto.calendar.anniversary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnniversariesResponse {

    @Schema(description = "그룹 id", example = "1")
    private Long groupId;

    @Schema(description = "기념일 시작 일자", example = "Date 타입")
    private LocalDate startDate;

    @Schema(description = "다음 기념일 일자", example = "Date 타입")
    private LocalDate nextDate;

    @Schema(description = "다음 기념일까지 남은 d-day", example = "234")
    private long dDay;

    @Schema(description = "기념일 일자부터 경과한 년수", example = "6")
    private long anniversaryYear;

    @Schema(description = "기념일 제목", example = "생일")
    private String title;
}
