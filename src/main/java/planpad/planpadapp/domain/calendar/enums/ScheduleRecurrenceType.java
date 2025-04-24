package planpad.planpadapp.domain.calendar.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ScheduleRecurrenceType {

    DAILY("매일"),
    WEEKDAYS("주중"),
    WEEKLY("매주"),
    MONTHLY("매월"),
    YEARLY("매년");

    private final String label;

    ScheduleRecurrenceType(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
