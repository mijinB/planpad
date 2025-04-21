package planpad.planpadapp.repository.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import planpad.planpadapp.domain.calendar.Anniversary;

@Repository
public interface AnniversaryRepository extends JpaRepository<Anniversary, Long> {
}
