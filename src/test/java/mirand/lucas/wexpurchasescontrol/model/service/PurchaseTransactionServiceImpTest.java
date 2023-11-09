package mirand.lucas.wexpurchasescontrol.model.service;

import mirand.lucas.wexpurchasescontrol.dto.DateIntervalDTO;
import mirand.lucas.wexpurchasescontrol.dto.ExchangedPurchaseDTO;
import mirand.lucas.wexpurchasescontrol.model.entity.PurchaseTransaction;
import mirand.lucas.wexpurchasescontrol.model.repository.PurchaseRepository;
import mirand.lucas.wexpurchasescontrol.model.services.CurrencyExchangeRateService;
import mirand.lucas.wexpurchasescontrol.model.services.PurchaseTransactionService;
import mirand.lucas.wexpurchasescontrol.model.services.imp.PurchaseTransactionServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseTransactionServiceImpTest {
    private static final String COUNTRY_BRAZIL = "Brazil";
    private static final String CURRENCY_REAL = "Real";
    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private CurrencyExchangeRateService currencyExchangeRateService;
    private PurchaseTransactionService service;

    @BeforeEach
    void setUp() {
        service = new PurchaseTransactionServiceImp(purchaseRepository, currencyExchangeRateService);
    }

    @Test
    public void testExchangeAndRoundAmountBellow() {
        BigDecimal amount = new BigDecimal(2.52);
        BigDecimal rate = new BigDecimal(5.033);
        BigDecimal expectedRounded =  new BigDecimal("12.68"); //12.68316

        BigDecimal resultRounded = service.exchangeAndRoundAmount(amount, rate);

        Assertions.assertEquals(0, expectedRounded.compareTo(resultRounded));
    }

    @Test
    public void testExchangeAndRoundAmountAbove() {
        BigDecimal amount = new BigDecimal(2.53);
        BigDecimal rate = new BigDecimal(5.034);
        BigDecimal expectedRounded =  new BigDecimal("12.74"); //12.73602

        BigDecimal resultRounded = service.exchangeAndRoundAmount(amount, rate);

        Assertions.assertEquals(0, expectedRounded.compareTo(resultRounded));
    }

    @Test
    public void testGetPurchaseByIdInCurrencyNoCountryNoCurrency() {
        final Long id = 42L;
        final LocalDate transactionDate = LocalDate.now().minusDays(1);
        final String description = "Test no currency";
        final BigDecimal purchaseAmount = new BigDecimal("8.91");

        PurchaseTransaction purchaseTransaction = new PurchaseTransaction();
        purchaseTransaction.setId(id);
        purchaseTransaction.setTransactionDate(transactionDate);
        purchaseTransaction.setDescription(description);
        purchaseTransaction.setPurchaseAmountUSD(purchaseAmount);

        when(purchaseRepository.getReferenceById(id)).thenReturn(purchaseTransaction);


//        when(currencyExchangeRateService.generateAcceptableInterval(any())).thenReturn(null);

//        when(currencyExchangeRateService.getNewestExchangeRateInInterval(any(), any(), any())).thenThrow(new RuntimeException("Should not access external API exchange rate without a country and currency"));


        ExchangedPurchaseDTO result = service.getPurchaseByIdInCurrency(id, null, null);

        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(transactionDate, result.getTransactionDate());
        Assertions.assertEquals(description, result.getDescription());
        Assertions.assertEquals(purchaseAmount, result.getPurchaseAmountUSD());
        Assertions.assertEquals(null, result.getExchangeRate());
        Assertions.assertEquals(null, result.getConvertedAmout());
    }



    @Test
    public void testGetPurchaseByIdInCurrencyNoCountryWithCurrency() {
        final Long id = 9L;
        final LocalDate transactionDate = LocalDate.now().minusDays(1);
        final String description = "Chocolate bar";
        final BigDecimal purchaseAmount = new BigDecimal("1.29");

        final BigDecimal dollarToRealRate = new BigDecimal("4.85");

        PurchaseTransaction purchaseTransaction = new PurchaseTransaction();
        purchaseTransaction.setId(id);
        purchaseTransaction.setTransactionDate(transactionDate);
        purchaseTransaction.setDescription(description);
        purchaseTransaction.setPurchaseAmountUSD(purchaseAmount);

        when(purchaseRepository.getReferenceById(id)).thenReturn(purchaseTransaction);

        DateIntervalDTO dateIntervalDTO = new DateIntervalDTO();
        dateIntervalDTO.setStart(transactionDate.minusMonths(6));
        dateIntervalDTO.setEnd(transactionDate);

        when(currencyExchangeRateService.generateAcceptableInterval(any())).thenAnswer(invocation -> {
            Object arg1 = invocation.getArgument(0);
            Assertions.assertEquals(transactionDate, arg1);
            return dateIntervalDTO;

        });


        when(currencyExchangeRateService.getNewestExchangeRateInInterval(
                any(), any(), any()))
                .thenAnswer(invocation -> {

                    Object arg1 = invocation.getArgument(0);
                    Object arg2 = invocation.getArgument(1);
                    Object arg3 = invocation.getArgument(2);

                    Assertions.assertEquals(COUNTRY_BRAZIL, arg1);
                    Assertions.assertEquals(CURRENCY_REAL, arg2);
                    Assertions.assertEquals(dateIntervalDTO, arg3);

                    return dollarToRealRate;
                });




        ExchangedPurchaseDTO result = service.getPurchaseByIdInCurrency(id, COUNTRY_BRAZIL, CURRENCY_REAL);

        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(transactionDate, result.getTransactionDate());
        Assertions.assertEquals(description, result.getDescription());
        Assertions.assertEquals(purchaseAmount, result.getPurchaseAmountUSD());
        Assertions.assertEquals(dollarToRealRate, result.getExchangeRate());


        BigDecimal converted = service.exchangeAndRoundAmount(result.getPurchaseAmountUSD(), result.getExchangeRate());
        Assertions.assertEquals(converted, result.getConvertedAmout());
    }
}
