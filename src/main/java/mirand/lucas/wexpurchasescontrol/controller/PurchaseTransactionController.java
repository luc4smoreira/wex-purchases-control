package mirand.lucas.wexpurchasescontrol.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import mirand.lucas.wexpurchasescontrol.dto.PurchaseDTO;
import mirand.lucas.wexpurchasescontrol.dto.ExchangedPurchaseDTO;
import mirand.lucas.wexpurchasescontrol.model.services.PurchaseTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PurchaseTransactionController {


    private final PurchaseTransactionService purchaseService;

    @Autowired
    public PurchaseTransactionController(PurchaseTransactionService service){
       this.purchaseService = service;
    }


    @PostMapping("/store")
    public Long createPurchase(@Valid @RequestBody PurchaseDTO purchaseDTO) {
        //TODO validate purchase with this requirement: Purchase amount: must be a valid positive amount rounded to the nearest cent
        return purchaseService.storePurchase(purchaseDTO);
    }

    @GetMapping("/get")
    public ExchangedPurchaseDTO getPurchase(@RequestParam(value = "id") @NotNull Long id, @RequestParam(value = "country") String country, @RequestParam(value = "currency") String currency) {
        //TODO If no currency conversion rate is available within 6 months equal to or before the purchase date, an error should be returned stating the purchase cannot be converted to the target currency.
        return purchaseService.getPurchaseByIdInCurrency(id, country, currency);

    }

}
