package planpad.planpadapp.service.memo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.Folder;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.memo.FolderDto;
import planpad.planpadapp.repository.memo.FolderRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    public void saveFolder(User user, FolderDto folderDto) {
        Folder folder = folderDto.toEntity(user);
        folderRepository.save(folder);
    }
}
