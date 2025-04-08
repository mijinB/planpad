package planpad.planpadapp.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.Group;
import planpad.planpadapp.dto.calendar.GroupRequestDto;
import planpad.planpadapp.repository.calendar.GroupRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    @Transactional
    public Long saveGroup(User user, GroupRequestDto data) {
        Group group = Group.builder()
                .user(user)
                .name(data.getName())
                .build();

        groupRepository.save(group);
        return group.getGroupId();
    }
}
