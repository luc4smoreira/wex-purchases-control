package miranda.lucas.wexpurchasescontrol.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="The purchase cannot be converted to the target currency.")
public class CurrencyExchangeNotAvailableException extends RuntimeException{

}
