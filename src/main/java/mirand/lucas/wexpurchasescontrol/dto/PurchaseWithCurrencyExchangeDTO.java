package mirand.lucas.wexpurchasescontrol.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Purchase with stored id, converted to a specific currency
 */
public class PurchaseWithCurrencyExchangeDTO extends PurchaseDTO {

    private Long id;
    private BigDecimal exchangeRate;
    @Positive
    private BigDecimal convertedAmout;

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

    public BigDecimal getConvertedAmout() {
        return convertedAmout;
    }

    public void setConvertedAmout(BigDecimal convertedAmout) {
        this.convertedAmout = convertedAmout;
    }
}
