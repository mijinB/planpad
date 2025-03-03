package planpad.planpadapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import planpad.planpadapp.repository.memo.TagRepository;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final TagRepository tagRepository;

    /*List<Tag> tags = dtoData.getTags().stream()
            .map(tag -> tagRepository.findByName(tag.getName())
                    .orElseGet(() -> tagRepository.save(new Tag(tag.getName()))))
            .collect(Collectors.toList());
            이렇게 tags를 변경하고 memo 생성자에 넣어서 생성하기*/
}
