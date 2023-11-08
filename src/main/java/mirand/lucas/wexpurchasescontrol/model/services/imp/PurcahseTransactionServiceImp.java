package mirand.lucas.wexpurchasescontrol.model.services.imp;

import mirand.lucas.wexpurchasescontrol.dto.PurchaseDTO;
import mirand.lucas.wexpurchasescontrol.dto.PurchaseWithCurrencyExchangeDTO;
import mirand.lucas.wexpurchasescontrol.exceptions.CurrencyExchangeNotAvailableException;
import mirand.lucas.wexpurchasescontrol.exceptions.PurchaseNotFoundException;
import mirand.lucas.wexpurchasescontrol.model.services.PurchaseTransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PurcahseTransactionServiceImp implements PurchaseTransactionService {
    @Override
    public Long storePurchase(PurchaseDTO purchaseDTO) {
        return 42L;
    }

    @Override
    public PurchaseWithCurrencyExchangeDTO getPurchaseByIdInCurrency(Long id, String country, String currency) throws CurrencyExchangeNotAvailableException, PurchaseNotFoundException {
        PurchaseWithCurrencyExchangeDTO purchaseWithCurrencyExchangeDTO = new PurchaseWithCurrencyExchangeDTO();
        purchaseWithCurrencyExchangeDTO.setExchangeRate(new BigDecimal(1.42));
        purchaseWithCurrencyExchangeDTO.setDescription("Teste");
        return purchaseWithCurrencyExchangeDTO;
    }
}
