package mirand.lucas.wexpurchasescontrol.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Purchase with stored id, converted to a specific currency
 */
public class ExchangedPurchaseDTO extends PurchaseDTO {

    private Long id;
    @Positive
    private BigDecimal exchangeRate;
    @Positive
    private BigDecimal convertedAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}
