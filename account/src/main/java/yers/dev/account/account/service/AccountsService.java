package yers.dev.account.account.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import yers.dev.account.auth.entity.dto.RegistrationRequest;
import yers.dev.account.account.entity.dto.AccountsDto;
import yers.dev.account.account.entity.Accounts;
import yers.dev.account.account.exception.ResourceNotFoundException;
import yers.dev.account.account.mapper.AccountsMapper;
import yers.dev.account.account.repository.AccountsRepository;
import java.util.List;

/**
 * Сервис для управления сущностями {@link Accounts}.
 * Предоставляет методы для получения списка пользователей,
 * поиска по email или keycloakId, а также для регистрации,
 * обновления, активации, деактивации и удаления аккаунтов.
 */
@Service
@AllArgsConstructor
public class AccountsService {

    private final AccountsRepository accountsRepository;

    /**
     * Возвращает список всех пользователей в виде DTO.
     *
     * @return список {@link AccountsDto} всех записей в базе
     */
    public List<AccountsDto> getAllUsers() {
        return AccountsMapper.mapToUserDto(accountsRepository.findAll());
    }

    /**
     * Ищет пользователя по адресу электронной почты.
     *
     * @param email электронная почта искомого пользователя
     * @return DTO пользователя
     * @throws ResourceNotFoundException если пользователя с таким email нет
     */
    public AccountsDto getUserByEmail(String email) {
        Accounts account = accountsRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "email", email));
        return AccountsMapper.mapToUserDto(account, new AccountsDto());
    }

    /**
     * Ищет пользователя по идентификатору в Keycloak.
     *
     * @param keycloakId идентификатор пользователя в Keycloak
     * @return DTO пользователя
     * @throws ResponseStatusException с кодом 404, если пользователь не найден
     */
    public AccountsDto getUserByKeycloakId(String keycloakId) {
        Accounts account = accountsRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Пользователь с id " + keycloakId + " не найден"
                ));
        return AccountsMapper.mapToUserDto(account, new AccountsDto());
    }

    /**
     * Регистрирует нового пользователя в локальной базе, создавая запись {@link Accounts}.
     *
     * @param req        регистрационные данные {@link RegistrationRequest}
     * @param keycloakId идентификатор созданного пользователя в Keycloak
     */
    @Transactional
    public void registerUser(RegistrationRequest req, String keycloakId) {
        Accounts account = new Accounts();
        account.setKeycloakId(keycloakId);
        account.setName(req.getFirstName());
        account.setLastName(req.getLastName());
        account.setEmail(req.getEmail());
        account.setPhoneNumber(Long.parseLong(req.getPhoneNumber()));
        account.setActive(true);
        accountsRepository.save(account);
    }

    /**
     * Обновляет данные существующего аккаунта по идентификатору Keycloak.
     *
     * @param req        DTO с новыми данными {@link RegistrationRequest}
     * @param keycloakId идентификатор пользователя в Keycloak
     * @throws ResponseStatusException с кодом 404, если аккаунт не найден
     */
    @Transactional
    public void KeycloakUpdateAccount(RegistrationRequest req, String keycloakId) {
        Accounts account = accountsRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found: " + keycloakId
                ));
        account.setName(req.getFirstName());
        account.setLastName(req.getLastName());
        account.setEmail(req.getEmail());
        account.setPhoneNumber(Long.parseLong(req.getPhoneNumber()));
        account.setActive(true);
        accountsRepository.save(account);
    }

    /**
     * Активирует аккаунт пользователя по его внутреннему ID.
     *
     * @param id внутренний идентификатор аккаунта
     * @throws RuntimeException если пользователь с таким ID не найден
     */
    @Transactional
    public void activateUser(Long id) {
        Accounts account = accountsRepository.findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", String.valueOf(id)));
        account.setActive(true);
        accountsRepository.save(account);
    }

    /**
     * Деактивирует аккаунт пользователя по его внутреннему ID.
     *
     * @param id внутренний идентификатор аккаунта
     * @throws RuntimeException если пользователь с таким ID не найден
     */
    @Transactional
    public void deactivateUser(Long id) {
        Accounts account = accountsRepository.findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", String.valueOf(id)));
        account.setActive(false);
        accountsRepository.save(account);
    }

    /**
     * Удаляет аккаунт пользователя по адресу электронной почты.
     *
     * @param email электронная почта пользователя
     * @throws RuntimeException если пользователь с таким email не найден
     */
    @Transactional
    public void deleteUser(String email) {
        Accounts account = accountsRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "email", email));
        accountsRepository.delete(account);
    }

}
