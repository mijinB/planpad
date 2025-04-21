package planpad.planpadapp.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.domain.calendar.Anniversary;
import planpad.planpadapp.domain.calendar.CalendarGroup;
import planpad.planpadapp.domain.calendar.ColorPalette;
import planpad.planpadapp.domain.calendar.enums.RecurrenceType;
import planpad.planpadapp.dto.calendar.anniversary.AnniversariesResponse;
import planpad.planpadapp.dto.calendar.anniversary.AnniversaryRequest;
import planpad.planpadapp.dto.calendar.anniversary.AnniversaryResponse;
import planpad.planpadapp.dto.calendar.anniversary.UpdateAnniversaryRequest;
import planpad.planpadapp.repository.calendar.AnniversaryRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnniversaryService {

    private final AnniversaryRepository anniversaryRepository;
    private final GroupService groupService;
    private final ColorPaletteService colorPaletteService;

    @Transactional
    public Long createAnniversary(User user, AnniversaryRequest data) {

        CalendarGroup group = groupService.getAuthorizedGroupOrThrow(user, data.getGroupId());
        ColorPalette colorPalette = colorPaletteService.getAuthorizedPaletteOrThrow(user, data.getPaletteId());

        Anniversary anniversary = Anniversary.builder()
                .user(user)
                .group(group)
                .colorPalette(colorPalette)
                .startDate(data.getStartDate())
                .endDate(data.getEndDate())
                .recurrenceType(data.getRecurrenceType())
                .title(data.getTitle())
                .build();
        anniversaryRepository.save(anniversary);

        return anniversary.getAnniversaryId();
    }

    public List<AnniversariesResponse> getAnniversaries(User user) {
        List<Anniversary> anniversaries = user.getAnniversaries();
        LocalDate today = LocalDate.now();

        return anniversaries.stream()
                .map(anniversary -> {
                    LocalDate startDate = anniversary.getStartDate();
                    LocalDate nextDate = calculateNextDate(startDate, anniversary.getRecurrenceType());
                    long dDay = ChronoUnit.DAYS.between(today, nextDate);
                    long anniversaryYear = ChronoUnit.YEARS.between(startDate, nextDate);

                    return new AnniversariesResponse(
                            anniversary.getGroup().getGroupId(),
                            startDate,
                            nextDate,
                            dDay,
                            anniversaryYear,
                            anniversary.getTitle()
                    );
                })
                .collect(Collectors.toList());
    }

    public AnniversaryResponse getAnniversary(User user, Long id) {
        Anniversary anniversary = getAuthorizedAnniversaryOrThrow(user, id);

        return new AnniversaryResponse(
                anniversary.getGroup().getGroupId(),
                anniversary.getColorPalette().getColorId(),
                anniversary.getStartDate(),
                anniversary.getEndDate(),
                anniversary.getRecurrenceType(),
                anniversary.getTitle()
        );
    }

    @Transactional
    public void updateAnniversary(User user, Long id, UpdateAnniversaryRequest data) {
        Anniversary anniversary = getAuthorizedAnniversaryOrThrow(user, id);
        CalendarGroup group = groupService.getAuthorizedGroupOrThrow(user, data.getGroupId());
        ColorPalette palette = colorPaletteService.getAuthorizedPaletteOrThrow(user, data.getPaletteId());

        anniversary.updateAnniversary(
                group,
                palette,
                data.getStartDate(),
                data.getEndDate(),
                data.getRecurrenceType(),
                data.getTitle()
        );
    }

    private LocalDate calculateNextDate(LocalDate startDate, RecurrenceType recurrenceType) {

        return switch (recurrenceType) {
            case YEARLY -> getNextYearlyDate(startDate);
            case D100 -> getNextDdayDate(startDate, 100);
            case D1000 -> getNextDdayDate(startDate, 1000);
        };
    }

    private LocalDate getNextYearlyDate(LocalDate startDate) {
        LocalDate today = LocalDate.now();
        LocalDate thisYear = startDate.withYear(today.getYear());

        return thisYear.isBefore(today) ? thisYear.plusYears(1) : thisYear;
    }

    private LocalDate getNextDdayDate(LocalDate startDate, int intervalDays) {
        LocalDate today = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(startDate, today);
        long nextInterval = (daysBetween / intervalDays) + 1;

        return startDate.plusDays(nextInterval * intervalDays);
    }

    private Anniversary getAuthorizedAnniversaryOrThrow(User user, Long id) {
        Anniversary anniversary = anniversaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("기념일을 찾을 수 없습니다."));

        if (!anniversary.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("해당 기념일에 접근할 수 없습니다.");
        }

        return anniversary;
    }
}
