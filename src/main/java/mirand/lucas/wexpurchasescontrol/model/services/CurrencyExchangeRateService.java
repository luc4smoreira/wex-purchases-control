package mirand.lucas.wexpurchasescontrol.model.services;

import jakarta.validation.constraints.NotNull;
import mirand.lucas.wexpurchasescontrol.dto.DateIntervalDTO;
import mirand.lucas.wexpurchasescontrol.exceptions.CurrencyExchangeNotAvailableException;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CurrencyExchangeRateService {


    /**
     * Retrieves the most recent exchange rate for the specified currency within the given date interval.
     *
     * @param country Country associated with a given exchange rate. (e.g., "Brazil", "Australia")
     * @param currency Currency associated with a given exchange rate. (e.g., "Real", "Dollar")
     * @param intervalDTO An object containing the start and end dates defining the interval for the query.
     * @return The most recent {@link BigDecimal} exchange rate for the specified currency in the given interval.
     * @throws CurrencyExchangeNotAvailableException if the specified currency is not found.
     */
    BigDecimal getNewestExchangeRateInInterval(@NotNull final String country, @NotNull final String currency, @NotNull DateIntervalDTO intervalDTO) throws CurrencyExchangeNotAvailableException;

    DateIntervalDTO generateAcceptableInterval(@NotNull LocalDate purchaseDate);

}
