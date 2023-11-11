package mirand.lucas.wexpurchasescontrol.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import mirand.lucas.wexpurchasescontrol.annotations.DecimalPlaces;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Purchase information
 */
public class PurchaseDTO {
    @Size(min = 1, max = 50)
    @NotNull(message = "description cannot be null")
    private String description;
    @Past
    @NotNull(message = "transactionDate cannot be null")
    private LocalDate transactionDate;
    @Positive
    @NotNull(message = "purchaseAmountUSD cannot be null")
    @DecimalPlaces(value = 2, message = "purchaseAmountUSD must be a valid positive amount rounded to the nearest cent.")
    private BigDecimal purchaseAmountUSD;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getPurchaseAmountUSD() {
        return purchaseAmountUSD;
    }

    public void setPurchaseAmountUSD(BigDecimal purchaseAmountUSD) {
        this.purchaseAmountUSD = purchaseAmountUSD;
    }
}
