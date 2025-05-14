package yers.dev.account.account.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * DTO для передачи информации об ошибке в ответе API.
 * Содержит сведения о пути запроса, коде и сообщении ошибки, а также о времени её возникновения.
 */
@Data
@AllArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "Схема, содержащая информацию об ошибке в ответе"
)
public class ErrorResponseDto {

    /**
     * Путь API, по которому был выполнен запрос, приведший к ошибке.
     */
    @Schema(description = "API path invoked by client")
    private String apiPath;

    /**
     * HTTP-статус, обозначающий тип ошибки.
     */
    @Schema(description = "Error code representing the error happened")
    private HttpStatus errorCode;

    /**
     * Текстовое сообщение с описанием причины ошибки.
     */
    @Schema(description = "Error message representing the error happened")
    private String errorMessage;

    /**
     * Момент времени, когда возникла ошибка.
     */
    @Schema(description = "Time representing when the error happened")
    private LocalDateTime errorTime;

}
