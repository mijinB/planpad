package planpad.planpadapp.dto.calendar.anniversary;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import planpad.planpadapp.domain.calendar.enums.RecurrenceType;

import java.time.LocalDate;

@Getter
public class AnniversaryRequest {

    @NotNull
    @Schema(description = "그룹 id", example = "1 (필수)")
    private Long groupId;

    @NotNull
    @Schema(description = "색상 팔레트 id", example = "1 (필수)")
    private Long paletteId;

    @NotNull
    @Schema(description = "기념일 시작 일자", example = "2025-04-14 (필수)")
    private LocalDate startDate;

    @NotNull
    @Schema(description = "기념일 종료 일자", example = "2025-04-14 (필수)")
    private LocalDate endDate;

    @NotNull
    @Schema(description = "기념일 반복 주기", example = "YEARLY or D100 or D1000 (中 1, 필수)")
    private RecurrenceType recurrenceType;

    @NotEmpty
    @Schema(description = "기념일 제목", example = "생일 (필수)")
    private String title;
}
