package yers.dev.account.auth.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


/**
 * DTO для аутентификации пользователя.
 * Используется при отправке данных на эндпоинты /auth/login.
 */
@Data
@Schema(name = "AuthRequest", description = "Authorization request")
public class AuthRequest {

    /**
     * Пароль пользователя.
     * <ul>
     *   <li>Не пустой</li>
     *   <li>Длина от 5 до 30 символов</li>
     *   <li>Должен содержать хотя бы одну букву</li>
     * </ul>
     */
    @Schema(description = "password", example = "Abc123456")
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
     * Электронная почта пользователя.
     */
    @Schema(description = "Email", example = "user@example.com")
    private String email;
}
