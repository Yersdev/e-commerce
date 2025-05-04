package yers.dev.account.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, сигнализирующее о том, что запрашиваемый ресурс не найден.
 * При выбрасывании этого исключения контроллер вернёт HTTP-статус 404 Not Found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Создаёт новое исключение, описывающее отсутствие ресурса.
     *
     * @param resourceName имя ресурса (например, "User", "Account" и т.д.)
     * @param fieldName    имя поля, по которому осуществлялся поиск (например, "id", "email")
     * @param fieldValue   значение поля, по которому ресурс не был найден
     */
    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format(
                "%s not found with the given input data %s : '%s'",
                resourceName, fieldName, fieldValue
        ));
    }
}
