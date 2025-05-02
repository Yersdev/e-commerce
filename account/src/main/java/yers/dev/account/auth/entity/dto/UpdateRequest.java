package yers.dev.account.auth.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для аутентификации и регистрации пользователя.
 * Используется при отправке данных на эндпоинты /auth/register, /auth/login и /user/me.
 */
@Data
@Schema(name = "RegistrationRequest", description = "RegistrationRequest")
public class UpdateRequest {

    /**
     * Пароль пользователя.
     */
    @Schema(description = "password", example = "123456789")
    private String password;

    /**
     * Имя пользователя.
     */
    @Schema(description = "First name", example = "John")
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    /**
     * Электронная почта пользователя.
     */
    @Schema(description = "Email", example = "lY6m6@example.com")
    private String email;


    /**
     * Номер телефона пользователя.
     */
    @Schema(description = "phone number", example = "+7-777-777-77-77")
    private String phoneNumber;
}
