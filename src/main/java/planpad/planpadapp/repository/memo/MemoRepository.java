package planpad.planpadapp.repository.memo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import planpad.planpadapp.domain.Memo;
import planpad.planpadapp.domain.User;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query("SELECT COALESCE(MAX(m.memoOrder) + 1, 1) FROM Memo m WHERE m.user = :user")
    Integer findNextOrderByUser(@Param("user") User user);
}
