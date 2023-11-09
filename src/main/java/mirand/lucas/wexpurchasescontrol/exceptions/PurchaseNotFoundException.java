package mirand.lucas.wexpurchasescontrol.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Purchase not found")
public class PurchaseNotFoundException extends RuntimeException {
}
