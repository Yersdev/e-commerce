package yers.dev.account.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yers.dev.account.auth.constant.HttpStatusConstants;
import yers.dev.account.auth.entity.dto.AuthRequest;
import yers.dev.account.auth.entity.dto.RegistrationRequest;
import yers.dev.account.auth.entity.dto.ErrorResponseDto;
import yers.dev.account.auth.service.AuthService;
import yers.dev.account.auth.service.KeycloakUserService;
import yers.dev.account.user.constants.AccountsConstants;
import yers.dev.account.user.entity.dto.ResponseDto;

import java.util.Map;

/**
 * Контроллер для аутентификации пользователей через Keycloak.
 * Содержит методы регистрации, входа, обновления токена и выхода.
 */
@Tag(
        name = "REST API for check Auth of user",
        description = "REST APIs to Auth user"
)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakUserService keycloakUserService;
    private final AuthService authService;

    @Operation(
            summary = "Registration",
            description = "REST API to register Account"
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
    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> register(@RequestBody RegistrationRequest req) {
        return ResponseEntity
                .status(HttpStatusConstants.USER_REGISTERED)
                .body(keycloakUserService.registerUser(req));
    }

    @Operation(
            summary = "Login",
            description = "REST API to login Account"
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
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthRequest req) {
        return ResponseEntity
                .status(HttpStatusConstants.USER_LOGGED_IN)
                .body(authService.login(req));
    }

    @Operation(
            summary = "Refresh Token",
            description = "REST API to refresh token"
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
    @PostMapping("/refresh")
    public ResponseEntity<Map<String,Object>> refresh(@RequestBody Map<String,String> body) {
        String refreshToken = body.get("refresh_token");
        return ResponseEntity
                .status(HttpStatusConstants.OK)
                .body(authService.refreshToken(refreshToken));
    }


    @Operation(
            summary = "Logout",
            description = "REST API to logout Account"
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
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@RequestBody Map<String,String> body) {
        authService.logout(body.get("refresh_token"));
        return ResponseEntity
                .status(HttpStatusConstants.OK)
                .body(new ResponseDto(AccountsConstants.STATUS_200_LOGOUT, AccountsConstants.MESSAGE_200_LOGOUT));
    }
}
