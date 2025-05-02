package yers.dev.account.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import yers.dev.account.auth.entity.dto.RegistrationRequest;
import yers.dev.account.dto.AccountsDto;
import yers.dev.account.entity.Accounts;
import yers.dev.account.exception.ResourceNotFoundException;
import yers.dev.account.mapper.AccountsMapper;
import yers.dev.account.repository.AccountsRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountsService {
    private final AccountsRepository accountsRepository;
    //private final KeycloakUserService keycloakUserService;

    public List<AccountsDto> getAllUsers() {
        return AccountsMapper.mapToUserDto(accountsRepository.findAll());
    }

    public AccountsDto getUserByEmail(String email) {
        return AccountsMapper.mapToUserDto(accountsRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Account", "email", email)), new AccountsDto());
    }


    /**
     * Получает информацию о текущем пользователе по его Keycloak ID.
     *
     * @param keycloakId идентификатор пользователя в Keycloak
     * @return данные пользователя в виде {@link AccountsDto}
     * @throws ResponseStatusException если пользователь не найден
     */
    public AccountsDto getUserByKeycloakId(String keycloakId) {
        Accounts accounts = accountsRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Пользователь с id " + keycloakId + " не найден"
                ));
        return AccountsMapper.mapToUserDto(accounts, new AccountsDto());
    }

    /**
     * Регистрирует нового пользователя в локальной базе данных.
     *
     * @param req        DTO с данными для регистрации
     * @param keycloakId ID пользователя в Keycloak
     */
    @Transactional
    public void registerUser(RegistrationRequest req, String keycloakId) {
        Accounts u = new Accounts();
        u.setKeycloakId(keycloakId);
        u.setName(req.getFirstName());
        u.setLastName(req.getLastName());
        u.setEmail(req.getEmail());
        u.setPhoneNumber(Long.parseLong(req.getPhoneNumber()));
        u.setActive(true);
        accountsRepository.save(u);
    }


    /**
     * Обновляет данные пользователя в базе по его Keycloak ID.
     *
     * @param req        DTO с обновлёнными данными
     * @param keycloakId ID пользователя в Keycloak
     * @throws ResponseStatusException если пользователь не найден
     */
    @Transactional
    public void KeycloakUpdateAccount(RegistrationRequest req , String keycloakId) {

        Accounts user = accountsRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found: " + keycloakId));
        user.setName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setEmail(req.getEmail());
        user.setPhoneNumber(Long.parseLong(req.getPhoneNumber()));
        user.setActive(true);
        accountsRepository.save(user);
    }

    @Transactional
    public void activateUser(Long id) {
        Accounts user = accountsRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        accountsRepository.save(user);
    }

    @Transactional
    public void deactivateUser(Long id) {
        Accounts user = accountsRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        accountsRepository.save(user);
    }


    @Transactional
    public void deleteUser(String email) {
        Accounts user = accountsRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        accountsRepository.delete(user);
    }

}