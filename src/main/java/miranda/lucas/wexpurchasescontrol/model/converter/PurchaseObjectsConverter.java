package miranda.lucas.wexpurchasescontrol.model.converter;

import miranda.lucas.wexpurchasescontrol.dto.PurchaseDTO;
import miranda.lucas.wexpurchasescontrol.dto.ExchangedPurchaseDTO;
import miranda.lucas.wexpurchasescontrol.model.entity.PurchaseTransaction;

public class PurchaseObjectsConverter {


    private PurchaseObjectsConverter() {
        //private constructor, only static methods
    }

    public static PurchaseTransaction toEntity(PurchaseDTO purchaseDTO) {
        PurchaseTransaction purchaseTransaction = new PurchaseTransaction();


        purchaseTransaction.setPurchaseAmountUsd(purchaseDTO.getPurchaseAmountUSD());
        purchaseTransaction.setDescription(purchaseDTO.getDescription());
        purchaseTransaction.setTransactionDate(purchaseDTO.getTransactionDate());

        return purchaseTransaction;
    }

    public static ExchangedPurchaseDTO fromEntity(PurchaseTransaction purchaseTransaction){
        ExchangedPurchaseDTO exchangedPurchaseDTO = new ExchangedPurchaseDTO();
        exchangedPurchaseDTO.setId(purchaseTransaction.getId());
        exchangedPurchaseDTO.setDescription(purchaseTransaction.getDescription());
        exchangedPurchaseDTO.setTransactionDate(purchaseTransaction.getTransactionDate());
        exchangedPurchaseDTO.setPurchaseAmountUSD(purchaseTransaction.getPurchaseAmountUsd());
        return exchangedPurchaseDTO;

    }
}
