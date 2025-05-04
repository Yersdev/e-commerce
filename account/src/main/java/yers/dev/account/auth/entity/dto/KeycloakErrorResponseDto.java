package yers.dev.account.auth.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * DTO для представления информации об ошибке, возвращаемой клиенту.
 * Используется в {@code @ExceptionHandler} или {@code @ControllerAdvice}
 * для структурированных ответов об ошибках.
 */
@Data
@AllArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "Schema to hold error response information"
)
public class KeycloakErrorResponseDto {

    /**
     * Путь API, который вызвал клиент.
     */
    @Schema(description = "API path invoked by client")
    private String apiPath;

    /**
     * HTTP статус ошибки.
     */
    @Schema(description = "Error code representing the error happened")
    private HttpStatus errorCode;

    /**
     * Сообщение об ошибке.
     */
    @Schema(description = "Error message representing the error happened")
    private String errorMessage;

    /**
     * Время, когда произошла ошибка.
     */
    @Schema(description = "Time representing when the error happened")
    private LocalDateTime errorTime;
}
