package mirand.lucas.wexpurchasescontrol.model.services;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CurrencyExchangeRateService {

    BigDecimal getCurrencyExchangeRateByDate(final String currency, final LocalDate date);

}
