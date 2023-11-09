package mirand.lucas.wexpurchasescontrol.dto;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Purchase information
 */
public class PurchaseDTO {
    @Size(max = 50)
    private String description;
    @Past
    private LocalDate transactionDate;
    @Positive
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
