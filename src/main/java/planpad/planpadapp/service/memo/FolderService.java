package planpad.planpadapp.service.memo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.Folder;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.memo.FolderDto;
import planpad.planpadapp.dto.memo.FolderResponseDto;
import planpad.planpadapp.dto.memo.FolderUpdateRequestDto;
import planpad.planpadapp.repository.memo.FolderRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    @Transactional
    public Long saveFolder(User user, FolderDto folderDto) {
        Integer nextOrder = folderRepository.findNextOrderByUser(user);
        Folder folder = new Folder(user, folderDto.getName(), nextOrder, folderDto.getColorCode());
        folderRepository.save(folder);
        return folder.getFolderId();
    }

    public List<FolderResponseDto> getFolders(User user) {
        List<Folder> folders = folderRepository.findAllByUser(user);

        return folders.stream()
                .map(FolderResponseDto::new)
                .collect(Collectors.toList());
    }

    public Optional<Folder> getFolder(Long id) {
        return folderRepository.findById(id);
    }

    @Transactional
    public void updateFolder(Long id, FolderUpdateRequestDto data) {

        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));

        folder.updateFolderInfo(data.getName(), data.getColorCode());

        if (data.getTargetOrder() != null && data.getNextOrder() != null) {
            changeFolderOrder(data.getTargetOrder(), data.getNextOrder());
        }
    }

    @Transactional
    public void changeFolderOrder(Integer targetOrder, Integer nextOrder) {

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
    }
}
