package planpad.planpadapp.dto.api.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planpad.planpadapp.dto.calendar.GroupsResponseDto;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupsResponseWrapper {
    private List<GroupsResponseDto> data;
    private String message;
}
