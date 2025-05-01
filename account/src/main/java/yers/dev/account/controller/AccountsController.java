package yers.dev.account.controller;

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
import yers.dev.account.constants.AccountsConstants;
import yers.dev.account.dto.ResponseDto;
import yers.dev.account.dto.AccountsDto;
import yers.dev.account.service.AccountsService;

import java.util.List;

@AllArgsConstructor
@RequestMapping(path="/api/accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RestController
public class AccountsController {
    private final AccountsService accountsService;
    private final KeycloakUserService keycloakUserService;


    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody AccountsDto accountsDto) {
        accountsService.createNewAccount(accountsDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody AccountsDto accountsDto) {
        boolean isUpdated = accountsService.updateUser(accountsDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
        }
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<ResponseDto> activateAccount(@PathVariable("id") Long id) {
        accountsService.activateUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccount(@Valid @RequestBody AccountsDto accountsDto) {
        accountsService.deleteUserById(accountsDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AccountsConstants.STATUS_DELETED_200, AccountsConstants.MESSAGE_DELETED_200));
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<AccountsDto> fetchAccountDetailsById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsService.getUserById(id));
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