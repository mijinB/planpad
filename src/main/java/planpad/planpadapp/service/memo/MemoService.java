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
import planpad.planpadapp.dto.memo.MemoResponseDto;
import planpad.planpadapp.dto.memo.MemoUpdateRequestDto;
import planpad.planpadapp.dto.memo.MemosResponseDto;
import planpad.planpadapp.repository.memo.MemoRepository;
import planpad.planpadapp.repository.memo.TagRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final FolderService folderService;
    private final TagService tagService;
    private final TagRepository tagRepository;

    @Transactional
    public Long saveMemo(User user, MemoRequestDto data) {
        Long folderId = data.getFolderId();
        Folder folder = folderService.getAuthorizedFolderOrThrow(user, folderId);

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

        if (data.getTags() != null) {
            tagService.saveTag(user, memo, data.getTags());
        }

        return memo.getMemoId();
    }

    public List<MemosResponseDto> getMemosByFolder(User user, Long folderId) {
        Folder folder = folderService.getAuthorizedFolderOrThrow(user, folderId);

        List<Memo> memos = memoRepository.findAllByFolder(folder);

        return memos.stream()
                .map(memo -> new MemosResponseDto(
                        memo.getMemoId(),
                        memo.getFolder().getFolderId(),
                        memo.getMemoOrder(),
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
        List<Memo> memos = memoRepository.findAllByUser(user);

        return memos.stream()
                .map(memo -> new MemosResponseDto(
                        memo.getMemoId(),
                        memo.getFolder().getFolderId(),
                        memo.getMemoOrder(),
                        memo.getTags().stream()
                                .map(Tag::getName)
                                .collect(Collectors.toList()),
                        memo.getTitle(),
                        memo.getContents(),
                        memo.isFixed()
                ))
                .collect(Collectors.toList());
    }

    public MemoResponseDto getMemo(User user, Long id) {
        Memo memo = getAuthorizedMemoOrThrow(user, id);

        return new MemoResponseDto(
                id,
                memo.getFolder().getFolderId(),
                memo.getTags().stream()
                        .map(Tag::getName)
                        .collect(Collectors.toList()),
                memo.getTitle(),
                memo.getContents(),
                memo.isFixed()
        );
    }

    @Transactional
    public void updateMemo(User user, Long id, MemoUpdateRequestDto data) {
        Memo memo = getAuthorizedMemoOrThrow(user, id);

        if (data.getFolderId() != null) {
            Folder folder = folderService.getFolderOrThrow(data.getFolderId());
            memo.updateMemoInfo(folder, data.getTitle(), data.getContents(), data.isFixed());
        }

        if (data.getTags() != null) {
            updateTags(user, memo, data.getTags());
        }

        if (data.getTargetOrder() != null && data.getNextOrder() != null) {
            changeMemoOrder(id, data.getTargetOrder(), data.getNextOrder());
        }
    }

    private void updateTags(User user, Memo memo, List<String> tags) {
        List<Tag> oldTags = new ArrayList<>(memo.getTags());
        memo.clearTags();

        for (Tag oldTag : oldTags) {
            if (oldTag.getMemos().isEmpty()) {
                tagRepository.delete(oldTag);
            }
        }

        tagService.saveTag(user, memo, tags);
    }

    public void changeMemoOrder(Long id,Integer targetOrder, Integer nextOrder) {

        if (targetOrder < nextOrder) {
            List<Memo> memos = memoRepository.findByMemoOrderBetween(targetOrder + 1, nextOrder);

            for (Memo memo : memos) {
                memo.updateMemoOrder(memo.getMemoOrder() - 1);
            }

        } else if (targetOrder > nextOrder) {
            List<Memo> memos = memoRepository.findByMemoOrderBetween(nextOrder, targetOrder - 1);

            for (Memo memo : memos) {
                memo.updateMemoOrder(memo.getMemoOrder() + 1);
            }
        }

        Memo targetMemo = getMemoOrThrow(id);
        targetMemo.updateMemoOrder(nextOrder);
    }

    @Transactional
    public void deleteMemo(User user, Long id) {
        Memo memo = getAuthorizedMemoOrThrow(user, id);

        Set<Tag> tags = new HashSet<>(memo.getTags());
        memo.clearTags();

        memoRepository.delete(memo);

        for (Tag tag : tags) {
            if (tag.getMemos().isEmpty()) {
                tagRepository.delete(tag);
            }
        }
    }

    @Transactional
    public void deleteMemos(User user, List<Long> ids) {
        List<Memo> memos = memoRepository.findAllById(ids);

        for (Long id : ids) {
            getAuthorizedMemoOrThrow(user, id);
        }

        for (Memo memo : memos) {
            memo.clearTags();

            Set<Tag> tags = new HashSet<>(memo.getTags());
            for (Tag tag : tags) {
                if (tag.getMemos().isEmpty()) {
                    tagRepository.delete(tag);
                }
            }
        }

        memoRepository.deleteAll(memos);
    }

    public Memo getMemoOrThrow(Long id) {

        return memoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메모를 찾을 수 없습니다."));
    }

    public Memo getAuthorizedMemoOrThrow(User user, Long id) {
        Memo memo = getMemoOrThrow(id);

        if (!memo.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("해당 메모에 접근할 수 없습니다.");
        }

        return memo;
    }
}
