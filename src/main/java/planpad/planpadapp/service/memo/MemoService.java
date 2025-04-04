package planpad.planpadapp.service.memo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.Folder;
import planpad.planpadapp.domain.Memo;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.memo.MemoRequestDto;
import planpad.planpadapp.dto.memo.MemoUpdateRequestDto;
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
    private final TagService tagService;

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
        tagService.saveTag(user, memo, data.getTags());

        return memo.getMemoId();
    }

    public List<MemosResponseDto> getMemosByFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));
        List<Memo> memos = memoRepository.findAllByFolder(folder);

        return memos.stream()
                .map(MemosResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<MemosResponseDto> getMemos(User user) {
        List<Memo> memos = memoRepository.findAllByUser(user);

        return memos.stream()
                .map(MemosResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateMemo(Long id, MemoUpdateRequestDto data) {
        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메모를 찾을 수 없습니다."));

        memo.updateMemoInfo(data.getTitle(), data.getContents(), data.isFixed());

        if (data.getTargetOrder() != null && data.getNextOrder() != null) {
            changeMemoOrder(data.getTargetOrder(), data.getNextOrder());
        }
    }

    public void changeMemoOrder(Integer targetOrder, Integer nextOrder) {

        if (targetOrder < nextOrder) {
            List<Memo> memos = memoRepository.findByMemoOrderBetween(targetOrder + 1, nextOrder);

            for (Memo memo : memos) {
                memo.updateMemoOrder(memo.getMemoOrder() - 1);
            }

        } else if (targetOrder > nextOrder) {
            List<Memo> memos = memoRepository.findByMemoOrderBetween(targetOrder - 1, nextOrder);

            for (Memo memo : memos) {
                memo.updateMemoOrder(memo.getMemoOrder() + 1);
            }
        }
    }
}
