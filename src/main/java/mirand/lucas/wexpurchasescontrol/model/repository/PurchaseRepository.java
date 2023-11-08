package mirand.lucas.wexpurchasescontrol.model.repository;

import mirand.lucas.wexpurchasescontrol.model.entity.PurchaseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseTransaction, Long> {
}
