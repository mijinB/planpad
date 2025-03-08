package planpad.planpadapp.repository.memo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import planpad.planpadapp.domain.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
}
