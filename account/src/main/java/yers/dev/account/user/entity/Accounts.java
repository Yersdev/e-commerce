package yers.dev.account.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность аккаунта пользователя.
 * Отображается в таблицу базы данных с полями для идентификатора пользователя,
 * привязки к Keycloak, контактной информации и статуса активности.
 */
@Entity
@Getter
@Setter
public class Accounts extends BaseEntity {

    /**
     * Первичный ключ аккаунта.
     * Значение генерируется базой данных (IDENTITY).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    /**
     * Идентификатор пользователя в Keycloak.
     * Должен быть уникальным и не может быть null.
     */
    @Column(unique = true, nullable = false)
    private String keycloakId;

    /**
     * Электронная почта пользователя.
     * Должна быть уникальной.
     */
    @Column(name = "email", unique = true)
    private String email;

    /**
     * Имя пользователя.
     * Обязательное поле.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Фамилия пользователя.
     * Обязательное поле.
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * Флаг, указывающий, активен ли аккаунт.
     * {@code true} — аккаунт активен, {@code false} — деактивирован.
     */
    private boolean isActive;

    /**
     * Номер телефона пользователя.
     * Допускаются цифры, пробелы, дефисы, скобки и необязательный ведущий '+'.
     */
    @Column(name = "phone_number")
    @Pattern(regexp = "\\+?[0-9\\s\\-()]+", message = "Phone number must be valid")
    private long phoneNumber;

}
