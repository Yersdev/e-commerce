package yers.dev.account.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yers.dev.account.auth.constant.HttpStatusConstants;
import yers.dev.account.auth.entity.dto.RegistrationRequest;
import yers.dev.account.auth.entity.dto.ErrorResponseDto;
import yers.dev.account.auth.service.KeycloakUserService;
import yers.dev.account.user.constants.AccountsConstants;
import yers.dev.account.user.entity.dto.ResponseDto;
import yers.dev.account.user.entity.dto.AccountsDto;
import yers.dev.account.user.service.AccountKeycloakService;
import yers.dev.account.user.service.AccountsService;

import java.util.List;

@AllArgsConstructor
@RequestMapping(path="/api/accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RestController
public class AccountsController {
    private final AccountsService accountsService;
    private final KeycloakUserService keycloakUserService;
    private final AccountKeycloakService accountKeycloakService;


    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody RegistrationRequest request) {
        accountKeycloakService.createNewAccount(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDto> updateAccountDetails(@PathVariable("id") Long id, @Valid @RequestBody RegistrationRequest request) {
        accountKeycloakService.updateUser(id, request);
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<ResponseDto> activateAccount(@PathVariable("id") Long id) {
        accountsService.activateUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<ResponseDto> deactivateAccount(@PathVariable("id") Long id) {
        accountsService.deactivateUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<ResponseDto> deleteAccount(@Valid @PathVariable("email") String email) {
        accountKeycloakService.deleteUser(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AccountsConstants.STATUS_DELETED_200, AccountsConstants.MESSAGE_DELETED_200));
    }


    @GetMapping("/fetch/email/{email}")
    public ResponseEntity<AccountsDto> fetchAccountDetailsByEmail(@PathVariable("email") String email) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsService.getUserByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<AccountsDto>> getAllAccounts() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsService.getAllUsers());
    }


    /**
     * Получить список всех пользователей.
     *
     * @return список всех пользователей в системе
     */
    @GetMapping("/all")
    public ResponseEntity<List<AccountsDto>> getAll(){
        return ResponseEntity
                .status(HttpStatusConstants.OK)
                .body(accountsService.getAllUsers());
    }

    @Operation(
            summary = "Fetch user by jwt",
            description = "REST API to fetch Account"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    /**
     * Получить данные текущего пользователя по JWT токену.
     *
     * @param jwt JWT токен, предоставленный через @AuthenticationPrincipal
     * @return данные текущего пользователя
     */
    @GetMapping("/me")
    public ResponseEntity<AccountsDto> getMe(
            @AuthenticationPrincipal Jwt jwt        // ← вот сюда Spring подставит ваш JWT
    ) {
        String keycloakId = jwt.getSubject();       // claim "sub" — UUID пользователя в Keycloak
        return ResponseEntity
                .status(HttpStatusConstants.OK)
                .body(accountsService.getUserByKeycloakId(keycloakId));
    }

    @Operation(
            summary = "Update user by jwt",
            description = "REST API to update Account"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )

    /**
     * Обновить данные текущего пользователя.
     *
     * @param jwt JWT токен с идентификатором пользователя (sub)
     * @param reqBody новые данные пользователя
     * @return пустой ответ с кодом 204 No Content
     */
    @PutMapping("/me")
    public ResponseEntity<HttpStatus> updateMe(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody RegistrationRequest reqBody
    ) {
        keycloakUserService.updateUser(jwt.getSubject(), reqBody);
        return ResponseEntity.noContent().build();
    }
}