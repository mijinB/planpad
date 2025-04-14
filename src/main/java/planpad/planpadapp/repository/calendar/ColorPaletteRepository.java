package planpad.planpadapp.repository.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import planpad.planpadapp.domain.calendar.ColorPalette;

@Repository
public interface ColorPaletteRepository extends JpaRepository<ColorPalette, Long> {
}
