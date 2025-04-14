package planpad.planpadapp.repository.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.CalendarGroup;

@Repository
public interface GroupRepository extends JpaRepository<CalendarGroup, Long> {

    boolean existsByUserAndName(User user, String name);
}
