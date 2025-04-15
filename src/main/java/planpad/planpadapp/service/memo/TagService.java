package planpad.planpadapp.service.memo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.memo.Memo;
import planpad.planpadapp.domain.memo.Tag;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.repository.memo.TagRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public void saveTag(User user, Memo memo, List<String> tags) {

        for (String tagName : tags) {
            Tag tag = tagRepository.findByUserAndName(user, tagName)
                    .orElseGet(() -> tagRepository.save(
                            Tag.builder()
                                    .user(user)
                                    .name(tagName)
                                    .build()));

            memo.addTag(tag);
        }
    }
}
