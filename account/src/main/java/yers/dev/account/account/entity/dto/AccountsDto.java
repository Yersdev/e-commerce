package yers.dev.account.account.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "AccountDto",
        description = "Data transfer object для передачи информации об аккаунте пользователя",
        requiredProperties = { "id", "email", "firstName", "lastName", "phoneNumber", "isActive" }
)
public class AccountsDto {

    /**
     * Уникальный идентификатор аккаунта.
     */
    private Long id;

    /**
     * Адрес электронной почты пользователя.
     * <p>Должен соответствовать корректному формату email.</p>
     */
    @Email(message = "Email must be valid")
    private String email;

    /**
     * Имя пользователя.
     * <p>Не должно быть пустым.</p>
     */
    @NotBlank(message = "First name must not be blank")
    private String firstName;

    /**
     * Фамилия пользователя.
     * <p>Не должна быть пустой.</p>
     */
    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    /**
     * Номер телефона пользователя.
     * <p>Допускаются цифры, пробелы, дефисы, скобки и необязательный ведущий знак '+'.</p>
     */
    @Pattern(regexp = "\\+?[0-9\\s\\-()]+", message = "Phone number must be valid")
    private long phoneNumber;

    /**
     * Флаг активности аккаунта.
     * <p>По умолчанию {@code true}.</p>
     */
    @Schema(defaultValue = "true")
    private boolean isActive;
}
