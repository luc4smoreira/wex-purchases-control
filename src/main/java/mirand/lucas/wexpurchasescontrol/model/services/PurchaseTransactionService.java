package mirand.lucas.wexpurchasescontrol.model.services;

import mirand.lucas.wexpurchasescontrol.dto.PurchaseDTO;
import mirand.lucas.wexpurchasescontrol.dto.PurchaseWithCurrencyExchangeDTO;
import mirand.lucas.wexpurchasescontrol.exceptions.CurrencyExchangeNotAvailableException;
import mirand.lucas.wexpurchasescontrol.exceptions.PurchaseNotFoundException;


public interface PurchaseTransactionService {

    /**
     * Persist the purchase and return the id associated with this entry
     *
     * @param purchaseDTO Information associated with the purchase in USD
     * @return Unique identifier: must uniquely identify the purchase
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
    PurchaseWithCurrencyExchangeDTO getPurchaseByIdInCurrency(final Long id, String country, final String currency) throws CurrencyExchangeNotAvailableException, PurchaseNotFoundException;
}
