package yers.dev.account.auth.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO для аутентификации и регистрации пользователя.
 * Используется при отправке данных на эндпоинты /auth/register, /auth/login и /user/me.
 */
@Data
@Schema(name = "RegistrationRequest", description = "Registration request")
public class RegistrationRequest {

    /**
     * Пароль пользователя.
     * <ul>
     *   <li>Не пустой</li>
     *   <li>Длина от 5 до 30 символов</li>
     *   <li>Должен содержать хотя бы одну букву</li>
     * </ul>
     */
    @Schema(description = "password", example = "Abc12345")
    @NotBlank(message = "Password must not be blank")
    @Size(
            min = 5,
            max = 30,
            message = "Password length must be between {min} and {max} characters"
    )
    @Pattern(
            regexp = ".*[A-Za-z].*",
            message = "Password must contain at least one letter"
    )
    private String password;

    /**
     * Имя пользователя.
     */
    @Schema(description = "First name", example = "John")
    @NotBlank(message = "First name must not be blank")
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    @Schema(description = "Last name", example = "Doe")
    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    /**
     * Электронная почта пользователя.
     */
    @Schema(description = "Email", example = "user@example.com")
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email must not be blank")
    private String email;

    /**
     * Номер телефона пользователя.
     */
    @Schema(description = "Phone number", example = "+7-777-777-77-77")
    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "\\+?[0-9\\s\\-()]+",
            message = "Phone number must be valid")
    private String phoneNumber;
}
