package mirand.lucas.wexpurchasescontrol.model.converter;

import mirand.lucas.wexpurchasescontrol.dto.PurchaseDTO;
import mirand.lucas.wexpurchasescontrol.dto.ExchangedPurchaseDTO;
import mirand.lucas.wexpurchasescontrol.model.entity.PurchaseTransaction;

public class PurchaseObjectsConverter {


    private PurchaseObjectsConverter() {
        //private constructor, only static methods
    }

    public static PurchaseTransaction toEntity(PurchaseDTO purchaseDTO) {
        PurchaseTransaction purchaseTransaction = new PurchaseTransaction();


        purchaseTransaction.setPurchaseAmount(purchaseDTO.getPurchaseAmount());
        purchaseTransaction.setDescription(purchaseDTO.getDescription());
        purchaseTransaction.setTransactionDate(purchaseDTO.getTransactionDate());

        return purchaseTransaction;
    }

    public static ExchangedPurchaseDTO fromEntity(PurchaseTransaction purchaseTransaction){
        ExchangedPurchaseDTO exchangedPurchaseDTO = new ExchangedPurchaseDTO();
        exchangedPurchaseDTO.setId(purchaseTransaction.getId());
        exchangedPurchaseDTO.setDescription(purchaseTransaction.getDescription());
        exchangedPurchaseDTO.setTransactionDate(purchaseTransaction.getTransactionDate());

        return exchangedPurchaseDTO;

    }
}
