package miranda.lucas.wexpurchasescontrol.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import miranda.lucas.wexpurchasescontrol.dto.PurchaseDTO;
import miranda.lucas.wexpurchasescontrol.dto.ExchangedPurchaseDTO;
import miranda.lucas.wexpurchasescontrol.exceptions.CurrencyExchangeNotAvailableException;
import miranda.lucas.wexpurchasescontrol.exceptions.PurchaseNotFoundException;
import miranda.lucas.wexpurchasescontrol.model.services.PurchaseTransactionService;
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

        ResponseEntity<?> responseEntity;
        try {
            ExchangedPurchaseDTO exchangedPurchaseDTO = purchaseService.getPurchaseByIdInCurrency(id, country, currency);
            responseEntity = new ResponseEntity<>(exchangedPurchaseDTO, HttpStatus.OK);
        }
        catch (CurrencyExchangeNotAvailableException e) {
            logger.info(String.format("Currency exchange not available for params. id: %d, country: %s, currency: %s", id, country, currency));
            responseEntity = new ResponseEntity<>("The purchase cannot be converted to the target currency. To access only the original purchase transaction, leave the country and currency fields empty.", HttpStatus.NOT_FOUND);
        }
        catch (PurchaseNotFoundException e) {
            logger.info(String.format("Purchase not found. id: %d", id));
            responseEntity = new ResponseEntity<>("Purchase not found.", HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

}
