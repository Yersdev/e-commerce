package yers.dev.account.auth.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
     */
    @Schema(description = "password", example = "123456789")
    private String password;

    /**
     * Электронная почта пользователя.
     */
    @Schema(description = "Email", example = "lY6m6@example.com")
    private String email;


}
