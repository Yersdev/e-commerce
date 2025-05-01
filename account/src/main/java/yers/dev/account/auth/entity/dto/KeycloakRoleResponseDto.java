package yers.dev.account.auth.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для представления информации о роли в Keycloak.
 * Используется для десериализации ответа от запроса
 * GET /admin/realms/{realm}/roles/{roleName}.
 * <p>Пример JSON-ответа:</p>
 * <pre>
 * {
 *   "id": "1cbfcb83-c49d-4826-a6fb-2b9ce96b5510",
 *   "name": "USER",
 *   "description": "Regular user"
 * }
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakRoleResponseDto {

    /** UUID роли в Keycloak */
    @Schema(example = "1cbfcb83-c49d-4826-a6fb-2b9ce96b5510")
    private String id;

    /** Название роли, например "USER" */
    @Schema(example = "USER")
    private String name;
}
