package planpad.planpadapp.service.memo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.Folder;
import planpad.planpadapp.domain.Memo;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.memo.MemoRequestDto;
import planpad.planpadapp.dto.memo.MemosResponseDto;
import planpad.planpadapp.repository.memo.FolderRepository;
import planpad.planpadapp.repository.memo.MemoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final FolderRepository folderRepository;

    @Transactional
    public Long saveMemo(User user, MemoRequestDto data) {
        Long folderId = data.getFolderId();
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));
        int nextOrder = memoRepository.findNextOrderByUser(user);

        Memo memo = Memo.builder()
                .user(user)
                .folder(folder)
                .memoOrder(nextOrder)
                .title(data.getTitle())
                .contents(data.getContents())
                .isFixed(data.isFixed())
                .build();

        memoRepository.save(memo);
        return memo.getMemoId();
    }

    public List<MemosResponseDto> getMemos(User user) {
        List<Memo> memos = memoRepository.findAllByUser(user);

        return memos.stream()
                .map(MemosResponseDto::new)
                .collect(Collectors.toList());
    }

    /*List<Tag> tags = dtoData.getTags().stream()
            .map(tag -> tagRepository.findByName(tag.getName())
                    .orElseGet(() -> tagRepository.save(new Tag(tag.getName()))))
            .collect(Collectors.toList());
            이렇게 tags를 변경하고 memo 생성자에 넣어서 생성하기*/
}
