package mirand.lucas.wexpurchasescontrol.controller;

import jakarta.validation.Valid;
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
        return purchaseService.storePurchase(purchaseDTO);
    }

    @GetMapping("/get")
    public ExchangedPurchaseDTO getPurchase(@RequestParam(value = "id") Long id, @RequestParam(value = "country") String country, @RequestParam(value = "currency") String currency) {
        return purchaseService.getPurchaseByIdInCurrency(id, country, currency);

    }

}
