package yers.dev.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class GeneralExternalCallException extends RuntimeException {

    public GeneralExternalCallException(String message) {
        super(message);
    }

}
