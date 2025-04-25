package planpad.planpadapp.dto.calendar.anniversary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import planpad.planpadapp.domain.calendar.enums.AnniversaryRecurrenceType;

import java.time.LocalDate;

@Getter
public class UpdateAnniversaryRequest {

    @Schema(description = "그룹 id", example = "1")
    private Long groupId;

    @Schema(description = "색상 팔레트 id", example = "#000000")
    private Long paletteId;

    @Schema(description = "기념일 시작 일자", example = "Date 타입")
    private LocalDate startDate;

    @Schema(description = "기념일 종료 일자", example = "Date 타입")
    private LocalDate endDate;

    @Schema(description = "기념일 반복 주기", example = "YEARLY or D100 or D1000")
    private AnniversaryRecurrenceType recurrenceType;

    @Schema(description = "기념일 제목", example = "생일")
    private String title;
}
