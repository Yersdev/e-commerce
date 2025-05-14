package yers.dev.account.account.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO для передачи информации об успешном результате выполнения API-запроса.
 * Содержит код статуса и сообщение для клиента.
 */
@Data
@AllArgsConstructor
@Schema(
        name = "Response",
        description = "Схема, содержащая информацию об успешном ответе"
)
public class ResponseDto {

    /**
     * Код статуса выполнения операции (например, "200", "201" и т.д.).
     */
    @Schema(description = "Status code in the response")
    private String statusCode;

    /**
     * Читаемое сообщение, описывающее результат (например, "OK", "Created").
     */
    @Schema(description = "Status message in the response")
    private String statusMsg;

}
