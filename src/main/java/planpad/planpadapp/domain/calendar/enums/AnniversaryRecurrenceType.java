package planpad.planpadapp.domain.calendar.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AnniversaryRecurrenceType {

    YEARLY("매년"),
    D100("100일"),
    D1000("1000일");

    private final String label;

    AnniversaryRecurrenceType(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
