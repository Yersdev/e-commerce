package yers.dev.account.user.constants;

/**
 * Contains constant values for HTTP response statuses and messages
 * used in account-related operations.
 * <p>
 * Статусы и сообщения сгруппированы по категориям:
 * <ul>
 *   <li>201 – создание аккаунта</li>
 *   <li>200 – общая успешная обработка запросов</li>
 *   <li>200_LOGOUT – успешный выход из системы</li>
 *   <li>DELETED_200 – успешное удаление аккаунта</li>
 * </ul>
 * Класс финальный и конструктор приватный, чтобы избежать создания экземпляров.
 * </p>
 */
public final class AccountsConstants {

    private AccountsConstants() {
        // prevent instantiation
    }

    /** HTTP статус 201. */
    public static final String STATUS_201 = "201";
    /** Сообщение при успешном создании аккаунта. */
    public static final String MESSAGE_201 = "Account created successfully";

    /** HTTP статус 200. */
    public static final String STATUS_200 = "200";
    /** Общий ответ при успешной обработке запроса. */
    public static final String MESSAGE_200 = "Request processed successfully";

    /** HTTP статус 200 при удалении аккаунта. */
    public static final String STATUS_DELETED_200 = "200";
    /** Сообщение при успешном удалении аккаунта. */
    public static final String MESSAGE_DELETED_200 = "Request processed successfully";
}
