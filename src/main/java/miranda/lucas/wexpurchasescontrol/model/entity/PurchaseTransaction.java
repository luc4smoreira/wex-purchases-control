package miranda.lucas.wexpurchasescontrol.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Purchase Transaction Entity
 * Field requirements:
 * Description: must not exceed 50 characters
 * Transaction date: must be a valid date format
 * Purchase amount: must be a valid positive amount rounded to the nearest cent
 * Unique identifier: must uniquely identify the purchase
 */
@Entity
public class PurchaseTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private LocalDate transactionDate;
    private BigDecimal purchaseAmountUsd;

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

    public BigDecimal getPurchaseAmountUsd() {
        return purchaseAmountUsd;
    }

    public void setPurchaseAmountUsd(BigDecimal purchaseAmount) {
        this.purchaseAmountUsd = purchaseAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
