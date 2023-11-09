package mirand.lucas.wexpurchasescontrol.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import mirand.lucas.wexpurchasescontrol.dto.PurchaseDTO;
import mirand.lucas.wexpurchasescontrol.dto.ExchangedPurchaseDTO;
import mirand.lucas.wexpurchasescontrol.model.services.PurchaseTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class PurchaseTransactionController {


    private final PurchaseTransactionService purchaseService;

    @Autowired
    public PurchaseTransactionController(PurchaseTransactionService service){
       this.purchaseService = service;
    }


    @PostMapping("/store")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createPurchase(@Valid @RequestBody PurchaseDTO purchaseDTO) {
        return purchaseService.storePurchase(purchaseDTO);
    }

    @GetMapping("/get")
    public ExchangedPurchaseDTO getPurchase(
            @RequestParam(value = "id") @NotNull Long id,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "currency", required = false) String currency) {

        return purchaseService.getPurchaseByIdInCurrency(id, country, currency);

    }

}
