package yers.dev.account.user.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import yers.dev.account.auth.entity.dto.RegistrationRequest;
import yers.dev.account.auth.service.KeycloakUserService;
import yers.dev.account.user.entity.Accounts;
import yers.dev.account.user.repository.AccountsRepository;

/**
 * Сервис для управления пользователями через Keycloak и локальное хранилище Accounts.
 * <p>
 * Предоставляет методы для создания, обновления и удаления учётных записей пользователей
 * с учётом синхронизации между Keycloak и базой данных приложения.
 * </p>
 */
@Service
@AllArgsConstructor
public class AccountKeycloakService {

    private final KeycloakUserService keycloakUserService;
    private final AccountsRepository accountsRepository;

    /**
     * Обновляет данные пользователя в Keycloak по его внутреннему ID.
     * <p>
     * Ищет запись {@link Accounts} в локальной базе по переданному {@code id}, извлекает
     * связанный {@code keycloakId} и передаёт в Keycloak новые данные из {@link RegistrationRequest}.
     * </p>
     *
     * @param id                  внутренний ID пользователя в базе Accounts
     * @param registrationRequest DTO с новыми данными пользователя (email, имя, роли и т.п.)
     * @throws RuntimeException если пользователь с таким {@code id} не найден в локальной базе
     */
    @Transactional
    public void updateUser(Long id, RegistrationRequest registrationRequest) {
        Accounts accounts = accountsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        keycloakUserService.updateUser(accounts.getKeycloakId(), registrationRequest);
    }

    /**
     * Создаёт нового пользователя в Keycloak на основании переданных регистрационных данных.
     * <p>
     * Не сохраняет локальную сущность Accounts — предполагается, что она будет создана
     * в другом месте после успешной регистрации в Keycloak.
     * </p>
     *
     * @param request DTO с данными для регистрации пользователя (email, пароль, роли и т.п.)
     * @throws jakarta.validation.ConstraintViolationException если данные {@code request} некорректны
     */
    @Transactional
    public void createNewAccount(@Valid RegistrationRequest request) {
        keycloakUserService.registerUser(request);
    }

    /**
     * Удаляет пользователя из Keycloak по адресу электронной почты.
     * <p>
     * Сначала проверяет наличие локальной записи в таблице Accounts, затем
     * извлекает {@code keycloakId} и передаёт его в Keycloak для удаления.
     * </p>
     *
     * @param email электронная почта пользователя
     * @throws RuntimeException если пользователь с таким {@code email} не найден в локальной базе
     */
    public void deleteUser(String email) {
        Accounts accounts = accountsRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        keycloakUserService.deleteUser(accounts.getKeycloakId());
    }
}
