package planpad.planpadapp.domain.calendar.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RecurrenceType {

    YEARLY("매년"),
    D100("100일"),
    D1000("1000일");

    private final String label;

    RecurrenceType(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
