package yers.dev.account.common.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import yers.dev.account.auth.entity.dto.KeycloakErrorResponseDto;
import yers.dev.account.auth.exception.FailedToDeleteKeycloak;
import yers.dev.account.user.entity.dto.ErrorResponseDto;
import yers.dev.account.user.exception.ResourceNotFoundException;
import yers.dev.account.user.exception.SameAccountExistException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Глобальный обработчик исключений для REST-контроллеров.
 * Перехватывает и формирует единый формат ответа для разных видов ошибок:
 * <ul>
 *     <li>Валидационные ошибки входных данных.</li>
 *     <li>Общие непойманные исключения.</li>
 *     <li>Ситуации, когда ресурс не найден.</li>
 *     <li>Конфликты при создании уже существующего аккаунта в Keycloak.</li>
 *     <li>Ошибка удаления пользователя из Keycloak.</li>
 * </ul>
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final WebRequest webRequest;

    /**
     * Конструирует обработчик с доступом к текущему {@link WebRequest}
     * для получения описания запроса при формировании ответа.
     *
     * @param webRequest текущий веб-запрос
     */
    public GlobalExceptionHandler(WebRequest webRequest) {
        this.webRequest = webRequest;
    }

    /**
     * Обрабатывает ошибки валидации аргументов контроллера (аннотация {@code @Valid}).
     * Собирает все поля с ошибками и возвращает карту «имя поля → сообщение ошибки».
     *
     * @param ex      исключение, содержащее результаты валидации {@link MethodArgumentNotValidException}
     * @param headers HTTP-заголовки, которые будут отправлены в ответе
     * @param status  исходный HTTP-код (обычно 400 BAD_REQUEST)
     * @param request исходный {@link WebRequest}
     * @return {@link ResponseEntity} со статусом {@code 400 Bad Request} и телом вида Map&lt;String, String&gt;
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });

        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает все непойманные исключения общего характера.
     * Возвращает структуру {@link ErrorResponseDto} с HTTP-статусом 500.
     *
     * @param e перехваченное исключение
     * @return {@link ResponseEntity} с {@link ErrorResponseDto} и статусом {@code 500 Internal Server Error}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception e) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обрабатывает исключение, когда запрошенный ресурс не найден.
     * Возвращает структуру {@link ErrorResponseDto} с HTTP-статусом 404.
     *
     * @param e исключение {@link ResourceNotFoundException}
     * @return {@link ResponseEntity} с {@link ErrorResponseDto} и статусом {@code 404 Not Found}
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает ситуацию, когда пытаются создать аккаунт,
     * который уже существует в Keycloak.
     * Возвращает структуру {@link KeycloakErrorResponseDto} с HTTP-статусом 409.
     *
     * @param exception  исключение {@link SameAccountExistException}
     * @param webRequest текущий веб-запрос
     * @return {@link ResponseEntity} с {@link KeycloakErrorResponseDto} и статусом {@code 409 Conflict}
     */
    @ExceptionHandler(SameAccountExistException.class)
    public ResponseEntity<KeycloakErrorResponseDto> handleSameAccountExistException(
            SameAccountExistException exception,
            WebRequest webRequest) {

        KeycloakErrorResponseDto keycloakErrorResponseDTO = new KeycloakErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.CONFLICT,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(keycloakErrorResponseDTO, HttpStatus.CONFLICT);
    }

    /**
     * Обрабатывает ошибку удаления пользователя из Keycloak.
     * Возвращает структуру {@link KeycloakErrorResponseDto} с HTTP-статусом 409.
     *
     * @param exception  исключение {@link FailedToDeleteKeycloak}
     * @param webRequest текущий веб-запрос
     * @return {@link ResponseEntity} с {@link KeycloakErrorResponseDto} и статусом {@code 409 Conflict}
     */
    @ExceptionHandler(FailedToDeleteKeycloak.class)
    public ResponseEntity<KeycloakErrorResponseDto> handleFailedToDeleteKeycloak(
            FailedToDeleteKeycloak exception,
            WebRequest webRequest) {

        KeycloakErrorResponseDto keycloakErrorResponseDTO = new KeycloakErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.CONFLICT,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(keycloakErrorResponseDTO, HttpStatus.CONFLICT);
    }
}
