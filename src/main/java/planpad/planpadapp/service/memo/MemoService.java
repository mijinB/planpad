package planpad.planpadapp.service.memo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.memo.Folder;
import planpad.planpadapp.domain.memo.Memo;
import planpad.planpadapp.domain.memo.Tag;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.memo.MemoRequest;
import planpad.planpadapp.dto.memo.MemoResponse;
import planpad.planpadapp.dto.memo.UpdateMemoRequest;
import planpad.planpadapp.dto.memo.MemosResponse;
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
    public Long createMemo(User user, MemoRequest data) {
        Long folderId = data.getFolderId();
        Folder folder = folderService.getAuthorizedFolderOrThrow(user, folderId);

        int nextOrder = memoRepository.findNextOrderByUser(user);

        Memo memo = Memo.builder()
                .user(user)
                .folder(folder)
                .memoOrder(nextOrder)
                .title(data.getTitle())
                .content(data.getContent())
                .isFixed(data.isFixed())
                .build();
        memoRepository.save(memo);

        if (data.getTags() != null) {
            tagService.createTag(user, memo, data.getTags());
        }

        return memo.getMemoId();
    }

    public List<MemosResponse> getMemosInFolder(User user, Long folderId) {
        Folder folder = folderService.getAuthorizedFolderOrThrow(user, folderId);

        List<Memo> memos = memoRepository.findAllByFolder(folder);

        return memos.stream()
                .map(memo ->  new MemosResponse(
                            memo.getMemoId(),
                            memo.getFolder().getFolderId(),
                            memo.getFolder().getColorCode(),
                            memo.getMemoOrder(),
                            memo.getTags().stream()
                                    .map(Tag::getName)
                                    .collect(Collectors.toList()),
                            memo.getTitle(),
                            memo.getContent(),
                            memo.isFixed()
                    ))
                .collect(Collectors.toList());
    }

    public List<MemosResponse> getMemosByUser(User user) {
        List<Memo> memos = memoRepository.findAllByUser(user);

        return memos.stream()
                .map(memo ->  new MemosResponse(
                            memo.getMemoId(),
                            memo.getFolder().getFolderId(),
                            memo.getFolder().getColorCode(),
                            memo.getMemoOrder(),
                            memo.getTags().stream()
                                    .map(Tag::getName)
                                    .collect(Collectors.toList()),
                            memo.getTitle(),
                            memo.getContent(),
                            memo.isFixed()
                    ))
                .collect(Collectors.toList());
    }

    public MemoResponse getMemo(User user, Long id) {
        Memo memo = getAuthorizedMemoOrThrow(user, id);

        return new MemoResponse(
                id,
                memo.getFolder().getFolderId(),
                memo.getFolder().getColorCode(),
                memo.getTags().stream()
                        .map(Tag::getName)
                        .collect(Collectors.toList()),
                memo.getTitle(),
                memo.getContent(),
                memo.isFixed()
        );
    }

    @Transactional
    public void updateMemo(User user, Long id, UpdateMemoRequest data) {
        Memo memo = getAuthorizedMemoOrThrow(user, id);

        if (data.getFolderId() != null) {
            Folder folder = folderService.getFolderOrThrow(data.getFolderId());
            memo.updateInfo(folder, data.getTitle(), data.getContent(), data.isFixed());
        } else {
            memo.updateInfo(null, data.getTitle(), data.getContent(), data.isFixed());
        }

        if (data.getTags() != null) {
            updateTags(user, memo, data.getTags());
        }

        if (data.getTargetOrder() != null && data.getNextOrder() != null) {
            updateMemoOrder(id, data.getTargetOrder(), data.getNextOrder());
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

        tagService.createTag(user, memo, tags);
    }

    public void updateMemoOrder(Long id,Integer targetOrder, Integer nextOrder) {

        if (targetOrder < nextOrder) {
            List<Memo> memos = memoRepository.findByMemoOrderBetween(targetOrder + 1, nextOrder);

            for (Memo memo : memos) {
                memo.changeOrder(memo.getMemoOrder() - 1);
            }

        } else if (targetOrder > nextOrder) {
            List<Memo> memos = memoRepository.findByMemoOrderBetween(nextOrder, targetOrder - 1);

            for (Memo memo : memos) {
                memo.changeOrder(memo.getMemoOrder() + 1);
            }
        }

        Memo targetMemo = getMemoOrThrow(id);
        targetMemo.changeOrder(nextOrder);
    }

    @Transactional
    public void moveMemosToFolder(User user, Long folderId, List<Long> memoIds) {
        Folder folder = folderService.getAuthorizedFolderOrThrow(user, folderId);
        List<Memo> memos = memoRepository.findAllById(memoIds);

        for (Memo memo : memos) {
            if (!memo.getUser().getUserId().equals(user.getUserId())) {
                throw new AccessDeniedException("권한이 없는 메모가 포함되어 있습니다.");
            }
            memo.moveToFolder(folder);
        }
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

        for (Memo memo : memos) {
            if (!memo.getUser().getUserId().equals(user.getUserId())) {
                throw new AccessDeniedException("권한이 없는 메모가 포함되어 있습니다.");
            }

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
