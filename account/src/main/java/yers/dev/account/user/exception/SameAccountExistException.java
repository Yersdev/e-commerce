package yers.dev.account.user.exception;

/**
 * Исключение, выбрасываемое при попытке зарегистрировать пользователя с уже существующим email.
 */
public class SameAccountExistException extends RuntimeException {

    /**
     * Конструктор исключения.
     *
     * @param email Email, который уже зарегистрирован в системе.
     */
    public SameAccountExistException(String email) {
        super(String.format("User with email %s already exist", email));
    }
}
