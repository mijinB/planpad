package planpad.planpadapp.repository.memo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import planpad.planpadapp.domain.Memo;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

}
