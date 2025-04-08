package planpad.planpadapp.repository.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import planpad.planpadapp.domain.calendar.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
