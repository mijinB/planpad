package planpad.planpadapp.service.memo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.memo.Folder;
import planpad.planpadapp.domain.memo.Memo;
import planpad.planpadapp.domain.memo.Tag;
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

        if (!folder.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("해당 폴더에 접근할 수 없습니다.");
        }

        int nextOrder = memoRepository.findNextOrderByUser(user);
        List<String> tags = data.getTags();

        Memo memo = Memo.builder()
                .user(user)
                .folder(folder)
                .memoOrder(nextOrder)
                .title(data.getTitle())
                .contents(data.getContents())
                .isFixed(data.isFixed())
                .build();
        memoRepository.save(memo);

        if (tags != null) {
            tagService.saveTag(user, memo, tags);
        }

        return memo.getMemoId();
    }

    public List<MemosResponseDto> getMemosByFolder(User user, Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));

        if (!folder.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("해당 폴더에 접근할 수 없습니다.");
        }

        return folder.getMemos().stream()
                .map(memo -> new MemosResponseDto(
                        memo.getTags().stream()
                                .map(Tag::getName)
                                .collect(Collectors.toList()),
                        memo.getTitle(),
                        memo.getContents(),
                        memo.isFixed()
                ))
                .collect(Collectors.toList());
    }

    public List<MemosResponseDto> getMemos(User user) {

        return user.getMemos().stream()
                .map(memo -> new MemosResponseDto(
                        memo.getTags().stream()
                                .map(Tag::getName)
                                .collect(Collectors.toList()),
                        memo.getTitle(),
                        memo.getContents(),
                        memo.isFixed()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateMemo(User user, Long id, MemoUpdateRequestDto data) {
        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메모를 찾을 수 없습니다."));
        Folder folder = folderRepository.findById(data.getFolderId())
                .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));

        if (!folder.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("해당 폴더에 접근할 수 없습니다.");
        }

        memo.updateMemoInfo(folder, data.getTitle(), data.getContents(), data.isFixed());

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

    @Transactional
    public void deleteMemo(User user, Long id) {
        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메모를 찾을 수 없습니다."));

        if (!memo.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("해당 메모에 접근할 수 없습니다.");
        }

        memoRepository.delete(memo);
    }

    @Transactional
    public void deleteMemoByUser(User user) {
        memoRepository.deleteAllByUser(user);
    }
}
