package planpad.planpadapp.repository.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import planpad.planpadapp.domain.calendar.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
