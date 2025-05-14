package yers.dev.account.auth.constant;

/**
 * Константы HTTP-статусов, используемые в ответах контроллеров.
 */
public class HttpStatusConstants {

    /**
     * Статус успешной регистрации пользователя (Created).
     */
    public static final int USER_REGISTERED = 201;

    /**
     * Общий статус успешного выполнения запроса (OK).
     */
    public static final int OK = 200;

    /**
     * Статус успешного входа пользователя (OK).
     * Продублирован OK, но используется для семантической ясности.
     */
    public static final int USER_LOGGED_IN = 200;


    /** HTTP статус 200 при выходе из системы. */
    public static final String STATUS_200_LOGOUT = "200";
    /** Сообщение при успешном логауте. */
    public static final String MESSAGE_200_LOGOUT = "Logout successful";
}
