package planpad.planpadapp.dto.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.domain.calendar.CalendarGroup;

@Getter
@NoArgsConstructor
public class GroupsResponseDto {

    @Schema(description = "그룹 id", example = "1")
    private Long id;

    @Schema(description = "그룹 이름", example = "group")
    private String name;

    public GroupsResponseDto(CalendarGroup group) {
        this.id = group.getGroupId();
        this.name = group.getName();
    }
}
