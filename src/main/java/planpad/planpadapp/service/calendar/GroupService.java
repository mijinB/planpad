package planpad.planpadapp.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.Group;
import planpad.planpadapp.dto.calendar.GroupRequestDto;
import planpad.planpadapp.dto.calendar.GroupsResponseDto;
import planpad.planpadapp.repository.calendar.GroupRepository;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<GroupsResponseDto> getGroups(User user) {

        return user.getGroups().stream()
                .map(GroupsResponseDto::new)
                .collect(Collectors.toList());
    }

    public void updateGroup(User user, Long id, GroupRequestDto data) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));

        if (!group.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("해당 그룹에 접근할 수 없습니다.");
        }

        group.updateGroup(data.getName());
    }

    public void deleteGroup(User user, Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));

        if (!group.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("해당 그룹에 접근할 수 없습니다.");
        }

        groupRepository.delete(group);
    }
}
