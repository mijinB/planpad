package planpad.planpadapp.service.memo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.Folder;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.memo.FolderDto;
import planpad.planpadapp.repository.memo.FolderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    @Transactional
    public void saveFolder(User user, FolderDto folderDto) {
        Integer nextOrder = folderRepository.findNextOrderByUser(user);
        Folder folder = folderDto.toEntity(user, nextOrder);
        folderRepository.save(folder);
    }

    public List<FolderDto> getFoldersByUser(User user) {
        List<Folder> folders = folderRepository.findAllByUser(user);

        return folders.stream()
                .map(FolderDto::new)
                .collect(Collectors.toList());
    }
}
