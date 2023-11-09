package mirand.lucas.wexpurchasescontrol.model.service;

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

@ExtendWith(MockitoExtension.class)
public class PurchaseTransactionServiceImpTest {

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
}
