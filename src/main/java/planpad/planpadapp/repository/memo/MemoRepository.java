package planpad.planpadapp.repository.memo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import planpad.planpadapp.domain.Folder;
import planpad.planpadapp.domain.Memo;
import planpad.planpadapp.domain.User;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query("SELECT m FROM Memo m WHERE m.user = :user ORDER BY m.memoOrder")
    List<Memo> findAllByUser(@Param("user") User user);

    @Query("SELECT m FROM Memo m WHERE m.folder = :folder ORDER BY m.memoOrder")
    List<Memo> findAllByFolder(@Param("folder") Folder folder);

    @Query("SELECT COALESCE(MAX(m.memoOrder) + 1, 1) FROM Memo m WHERE m.user = :user")
    Integer findNextOrderByUser(@Param("user") User user);

    List<Memo> findByMemoOrderBetween(Integer targetOrder, Integer nextOrder);
}
