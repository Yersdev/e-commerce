package yers.dev.account.service;

import jakarta.transaction.Transactional;
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
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountsService {
    private final AccountsRepository accountsRepository;

    public void deleteUserById(AccountsDto accountsDto) {
        accountsRepository.findByUserId(accountsRepository.findByEmail(accountsDto.getEmail()).orElseThrow(() -> new RuntimeException("User not found")).getUserId());
    }

    public List<AccountsDto> getAllUsers() {
        return AccountsMapper.mapToUserDto(accountsRepository.findAll());
    }

    public AccountsDto getUserByEmail(String email) {
        return AccountsMapper.mapToUserDto(accountsRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Account", "email", email)), new AccountsDto());
    }

    public AccountsDto getUserById(Long userId) {
        Accounts user = accountsRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return AccountsMapper.mapToUserDto(user, new AccountsDto());
    }

    @Transactional
    public boolean updateUser(AccountsDto accountsDto) {
        Accounts user = accountsRepository.findByEmail(accountsDto.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        AccountsMapper.mapToUser(user, accountsDto);
        accountsRepository.save(user);
        return true;
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
        accountsRepository.save(u);
    }

    @Transactional
    public boolean createNewAccount(AccountsDto accountsDto) {
        Accounts user = new Accounts();
        AccountsMapper.mapToUser(user, accountsDto);
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        accountsRepository.save(user);
        return true;
    }

    /**
     * Обновляет данные пользователя в базе по его Keycloak ID.
     *
     * @param req        DTO с обновлёнными данными
     * @param keycloakId ID пользователя в Keycloak
     * @throws ResponseStatusException если пользователь не найден
     */
    @Transactional
    public void updateAccount(RegistrationRequest req , String keycloakId) {
        Accounts user = accountsRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found: " + keycloakId));
        user.setName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setEmail(req.getEmail());
        user.setPhoneNumber(Long.parseLong(req.getPhoneNumber()));
        accountsRepository.save(user);
    }

    public void activateUser(Long id) {
        Accounts user = accountsRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        accountsRepository.save(user);
    }




}
