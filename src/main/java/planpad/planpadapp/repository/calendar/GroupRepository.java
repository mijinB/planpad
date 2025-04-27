package planpad.planpadapp.repository.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.CalendarGroup;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<CalendarGroup, Long> {

    @Query("SELECT g FROM CalendarGroup g WHERE g.user = :user ORDER BY g.groupId")
    List<CalendarGroup> findAllByUser(@Param("user") User user);

    boolean existsByUserAndName(User user, String name);
}
