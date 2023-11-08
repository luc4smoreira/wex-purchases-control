package mirand.lucas.wexpurchasescontrol.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/**
 * Holds the date interval acceptable to get an exchange rate associated with a purchase transaction
 */
public class DateIntervalDTO {
    @NotNull(message = "Start date cannot be null.")
    @Past(message = "Start date must be in the past.")
    private LocalDate start;
    @NotNull(message = "End date cannot be null.")
    @PastOrPresent(message = "End date must be in the past or present.")
    private LocalDate end;

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}
