package mirand.lucas.wexpurchasescontrol.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import mirand.lucas.wexpurchasescontrol.dto.PurchaseDTO;
import mirand.lucas.wexpurchasescontrol.dto.ExchangedPurchaseDTO;
import mirand.lucas.wexpurchasescontrol.exceptions.CurrencyExchangeNotAvailableException;
import mirand.lucas.wexpurchasescontrol.exceptions.PurchaseNotFoundException;
import mirand.lucas.wexpurchasescontrol.model.services.PurchaseTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PurchaseTransactionController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseTransactionController.class);

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
    public ResponseEntity<?> getPurchase(
            @RequestParam(value = "id") @NotNull Long id,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "currency", required = false) String currency) {


        try {
            ExchangedPurchaseDTO exchangedPurchaseDTO = purchaseService.getPurchaseByIdInCurrency(id, country, currency);
            return new ResponseEntity<>(exchangedPurchaseDTO, HttpStatus.OK);
        }
        catch (CurrencyExchangeNotAvailableException e) {
            logger.info(String.format("Currency exchange not available for params. id: %d, country: %s, currency: %s", id, country, currency));
            return new ResponseEntity<>("The purchase cannot be converted to the target currency. To access only the original purchase transaction, leave the country and currency fields empty.", HttpStatus.NOT_FOUND);
        }
        catch (PurchaseNotFoundException e) {
            logger.info(String.format("Purchase not found. id: %d", id));
            return new ResponseEntity<>("Purchase not found.", HttpStatus.NOT_FOUND);
        }

    }

}
