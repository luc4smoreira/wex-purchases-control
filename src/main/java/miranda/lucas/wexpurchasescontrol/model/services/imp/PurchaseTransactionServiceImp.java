package miranda.lucas.wexpurchasescontrol.model.services.imp;

import miranda.lucas.wexpurchasescontrol.dto.DateIntervalDTO;
import miranda.lucas.wexpurchasescontrol.dto.ExchangedPurchaseDTO;
import miranda.lucas.wexpurchasescontrol.dto.PurchaseDTO;
import miranda.lucas.wexpurchasescontrol.exceptions.CurrencyExchangeNotAvailableException;
import miranda.lucas.wexpurchasescontrol.exceptions.PurchaseNotFoundException;
import miranda.lucas.wexpurchasescontrol.model.converter.PurchaseObjectsConverter;
import miranda.lucas.wexpurchasescontrol.model.entity.PurchaseTransaction;
import miranda.lucas.wexpurchasescontrol.model.dao.PurchaseDAO;
import miranda.lucas.wexpurchasescontrol.model.services.CurrencyExchangeRateService;
import miranda.lucas.wexpurchasescontrol.model.services.PurchaseTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PurchaseTransactionServiceImp implements PurchaseTransactionService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseTransactionServiceImp.class);

    private static final int TOTAL_DECIMALS_FOR_CENTS = 2;
    private final CurrencyExchangeRateService currencyExchangeRateService;

    private final PurchaseDAO purchaseDAO;

    @Autowired
    public PurchaseTransactionServiceImp(PurchaseDAO purchaseDAO, CurrencyExchangeRateService currencyExchangeRateService) {
        this.purchaseDAO = purchaseDAO;
        this.currencyExchangeRateService = currencyExchangeRateService;

    }




    @Override
    public Long storePurchase(PurchaseDTO purchaseDTO) {
        PurchaseTransaction purchaseTransaction = PurchaseObjectsConverter.toEntity(purchaseDTO);
        purchaseDAO.insertPurchase(purchaseTransaction);
        return purchaseTransaction.getId();
    }




    @Override
    public ExchangedPurchaseDTO getPurchaseByIdInCurrency(final Long id, final String country, final String currency) throws CurrencyExchangeNotAvailableException, PurchaseNotFoundException {
        //first, get the purchase from database
        PurchaseTransaction storedPurchase = purchaseDAO.findPurchaseById(id);
        if(storedPurchase==null) {
            throw new PurchaseNotFoundException();
        }

        ExchangedPurchaseDTO exchangedPurchaseDTO = PurchaseObjectsConverter.fromEntity(storedPurchase);

        if(country != null && currency!=null) {

            //then, get the date interval to get the currency
            DateIntervalDTO dateIntervalDTO = currencyExchangeRateService.generateAcceptableInterval(storedPurchase.getTransactionDate());

            //get the exchange rate for the defined country and currency using am acceptable interval for this rate
            BigDecimal rate = currencyExchangeRateService.getNewestExchangeRateInInterval(country, currency, dateIntervalDTO);
            if(rate==null) {
                //If no currency conversion rate is available within 6 months equal to or before the purchase date, an error should be returned stating the purchase cannot be converted to the target currency.
                logger.debug(String.format("CurrencyExchangeNotAvailableException country: %s, currency: %s, interval-start: %s, internval-end:%s", country, currency, dateIntervalDTO.getStart().toString(), dateIntervalDTO.getEnd().toString()));
                throw new CurrencyExchangeNotAvailableException();
            }

            //set the exchage rate
            exchangedPurchaseDTO.setExchangeRate(rate);

            BigDecimal exchanged = exchangeAndRoundAmount(storedPurchase.getPurchaseAmountUsd(), rate);

            exchangedPurchaseDTO.setConvertedAmount(exchanged);
        }


        return exchangedPurchaseDTO;
    }


    @Override
    public BigDecimal exchangeAndRoundAmount(BigDecimal amount, BigDecimal exchangeRate) {
       //The converted purchase amount to the target currency should be rounded to two decimal places (i.e., cent).
       return amount.multiply(exchangeRate).setScale(TOTAL_DECIMALS_FOR_CENTS, RoundingMode.HALF_EVEN);
    }
}
