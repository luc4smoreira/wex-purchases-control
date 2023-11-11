package miranda.lucas.wexpurchasescontrol.model.services;

import jakarta.validation.constraints.NotNull;
import miranda.lucas.wexpurchasescontrol.dto.PurchaseDTO;
import miranda.lucas.wexpurchasescontrol.dto.ExchangedPurchaseDTO;
import miranda.lucas.wexpurchasescontrol.exceptions.CurrencyExchangeNotAvailableException;
import miranda.lucas.wexpurchasescontrol.exceptions.PurchaseNotFoundException;

import java.math.BigDecimal;


public interface PurchaseTransactionService {

    /**
     * Persist the purchase and return the id associated with this entry
     *
     * @param purchaseDTO Information associated with the purchase in USD
     * @return the id as a Unique identifier: must uniquely identify the purchase
     */
    Long storePurchase(final PurchaseDTO purchaseDTO);

    /**
     *
     * @param id PurchaseTransaction id to be searched in the database
     * @param country Country associated with a given exchange rate.
     * @param currency Currency name to be used to get the purchase. It can be null so the currency will the default USD
     *
     * @return PurchaseTransaction with the value in USD and the conversion value using the exchange
     * @throws CurrencyExchangeNotAvailableException the currency exchange rate is not avaliable for some reason
     * @throws PurchaseNotFoundException  the purchase id wasn´t found in the database
     */
    ExchangedPurchaseDTO getPurchaseByIdInCurrency(@NotNull final Long id, String country, final String currency) throws CurrencyExchangeNotAvailableException, PurchaseNotFoundException;


    /**
     * Converts the given amount using the specified exchange rate and rounds the result to the nearest cent.
     *
     * @param amount the amount to be converted, must be a positive value
     * @param exchangeRate the exchange rate to apply
     * @return the converted and rounded amount
     *
     */
    BigDecimal exchangeAndRoundAmount(BigDecimal amount, BigDecimal exchangeRate);
}
