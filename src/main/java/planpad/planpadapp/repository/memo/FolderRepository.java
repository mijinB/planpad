package planpad.planpadapp.repository.memo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import planpad.planpadapp.domain.Folder;
import planpad.planpadapp.domain.User;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query("SELECT f FROM Folder f WHERE f.user = :user ORDER BY f.folderOrder")
    List<Folder> findAllByUser(@Param("user") User user);

    @Query("SELECT COALESCE(MAX(f.folderOrder) + 1, 1) FROM Folder f WHERE f.user = :user")
    Integer findNextOrderByUser(@Param("user") User user);

    List<Folder> findByFolderOrderBetween(Integer startOrder, Integer endOrder);
}
