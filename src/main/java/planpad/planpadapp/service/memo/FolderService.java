package planpad.planpadapp.service.memo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.memo.Folder;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.memo.FolderRequestDto;
import planpad.planpadapp.dto.memo.FoldersResponseDto;
import planpad.planpadapp.dto.memo.FolderUpdateRequestDto;
import planpad.planpadapp.repository.memo.FolderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    @Transactional
    public Long saveFolder(User user, FolderRequestDto data) {

        boolean exists = folderRepository.existsByUserAndName(user, data.getName());
        if (exists) {
            throw new IllegalArgumentException("이미 같은 이름의 폴더가 존재합니다.");
        }

        Integer nextOrder = folderRepository.findNextOrderByUser(user);
        Folder folder = Folder.builder()
                .user(user)
                .name(data.getName())
                .colorCode(data.getColorCode())
                .folderOrder(nextOrder)
                .build();

        folderRepository.save(folder);
        return folder.getFolderId();
    }

    @Transactional
    public void saveDefaultFolder(User user) {
        Folder defaultFolder = Folder.builder()
                .user(user)
                .name("내 메모")
                .colorCode("#D3D3D3")
                .folderOrder(1)
                .build();

        folderRepository.save(defaultFolder);
    }

    public List<FoldersResponseDto> getFolders(User user) {
        List<Folder> folders = folderRepository.findAllByUser(user);

        return folders.stream()
                .map(FoldersResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateFolder(User user, Long id, FolderUpdateRequestDto data) {
        Folder folder = getAuthorizedFolderOrThrow(user, id);
        folder.updateFolderInfo(data.getName(), data.getColorCode());

        if (data.getTargetOrder() != null && data.getNextOrder() != null) {
            changeFolderOrder(id, data.getTargetOrder(), data.getNextOrder());
        }
    }

    public void changeFolderOrder(Long id,Integer targetOrder, Integer nextOrder) {

        if (targetOrder < nextOrder) {
            List<Folder> folders = folderRepository.findByFolderOrderBetween(targetOrder + 1, nextOrder);

            for (Folder folder : folders) {
                folder.updateFolderOrder(folder.getFolderOrder() - 1);
            }

        } else if (targetOrder > nextOrder) {
            List<Folder> folders = folderRepository.findByFolderOrderBetween(nextOrder, targetOrder - 1);

            for (Folder folder : folders) {
                folder.updateFolderOrder(folder.getFolderOrder() + 1);
            }
        }

        Folder targetFolder = getFolderOrThrow(id);
        targetFolder.updateFolderOrder(nextOrder);
    }

    @Transactional
    public void deleteFolder(User user, Long id) {
        Folder folder = getAuthorizedFolderOrThrow(user, id);
        folderRepository.delete(folder);
    }

    @Transactional
    public void deleteFolderByUser(User user) {
        folderRepository.deleteAllByUser(user);
    }

    public Folder getFolderOrThrow(Long id) {

        return folderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));
    }

    public Folder getAuthorizedFolderOrThrow(User user, Long id) {
        Folder folder = getFolderOrThrow(id);

        if (!folder.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("해당 폴더에 접근할 수 없습니다.");
        }

        return folder;
    }
}
