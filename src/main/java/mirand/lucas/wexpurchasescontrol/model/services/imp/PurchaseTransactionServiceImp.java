package mirand.lucas.wexpurchasescontrol.model.services.imp;

import mirand.lucas.wexpurchasescontrol.dto.DateIntervalDTO;
import mirand.lucas.wexpurchasescontrol.dto.ExchangedPurchaseDTO;
import mirand.lucas.wexpurchasescontrol.dto.PurchaseDTO;
import mirand.lucas.wexpurchasescontrol.exceptions.CurrencyExchangeNotAvailableException;
import mirand.lucas.wexpurchasescontrol.exceptions.PurchaseNotFoundException;
import mirand.lucas.wexpurchasescontrol.model.converter.PurchaseObjectsConverter;
import mirand.lucas.wexpurchasescontrol.model.entity.PurchaseTransaction;
import mirand.lucas.wexpurchasescontrol.model.repository.PurchaseRepository;
import mirand.lucas.wexpurchasescontrol.model.services.CurrencyExchangeRateService;
import mirand.lucas.wexpurchasescontrol.model.services.PurchaseTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class PurchaseTransactionServiceImp implements PurchaseTransactionService {

    private static final int TOTAL_DECIMALS_FOR_CENTS = 2;
    private final CurrencyExchangeRateService currencyExchangeRateService;

    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseTransactionServiceImp(PurchaseRepository purchaseRepository, CurrencyExchangeRateService currencyExchangeRateService) {
        this.purchaseRepository = purchaseRepository;
        this.currencyExchangeRateService = currencyExchangeRateService;

    }




    @Override
    public Long storePurchase(PurchaseDTO purchaseDTO) {
        PurchaseTransaction purchaseTransaction = PurchaseObjectsConverter.toEntity(purchaseDTO);
        purchaseRepository.save(purchaseTransaction);
        return purchaseTransaction.getId();
    }




    @Override
    public ExchangedPurchaseDTO getPurchaseByIdInCurrency(final Long id, final String country, final String currency) throws CurrencyExchangeNotAvailableException, PurchaseNotFoundException {
        //first, get the purchase from database
        PurchaseTransaction storedPurchase = purchaseRepository.getReferenceById(id);

        //then, get the date interval to get the currency
        DateIntervalDTO dateIntervalDTO = currencyExchangeRateService.generateAcceptableInterval(storedPurchase.getTransactionDate());

        ExchangedPurchaseDTO exchangedPurchaseDTO = PurchaseObjectsConverter.fromEntity(storedPurchase);

        if(country != null && currency!=null) {
            //get the exchange rate for the defined country and currency using am acceptable interval for this rate
            BigDecimal rate = currencyExchangeRateService.getNewestExchangeRateInInterval(country, currency, dateIntervalDTO);

            //set the exchage rate
            exchangedPurchaseDTO.setExchangeRate(rate);

            BigDecimal exchanged = exchangeAndRoundAmount(storedPurchase.getPurchaseAmountUSD(), rate);

            exchangedPurchaseDTO.setConvertedAmout(exchanged);
        }


        return exchangedPurchaseDTO;
    }


    @Override
    public BigDecimal exchangeAndRoundAmount(BigDecimal amount, BigDecimal exchangeRate) {
       //TODO The converted purchase amount to the target currency should be rounded to two decimal places (i.e., cent).
       return amount.multiply(exchangeRate).setScale(TOTAL_DECIMALS_FOR_CENTS);
    }
}