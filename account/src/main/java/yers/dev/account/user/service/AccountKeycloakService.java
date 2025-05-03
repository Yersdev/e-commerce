package yers.dev.account.user.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import yers.dev.account.auth.entity.dto.RegistrationRequest;
import yers.dev.account.auth.service.KeycloakUserService;
import yers.dev.account.user.entity.Accounts;
import yers.dev.account.user.repository.AccountsRepository;

@Service
@AllArgsConstructor
public class AccountKeycloakService {

    private final KeycloakUserService keycloakUserService;
    private final AccountsRepository accountsRepository;

    @Transactional
    public void updateUser(Long id, RegistrationRequest registrationRequest) {
        Accounts accounts = accountsRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        keycloakUserService.updateUser(accounts.getKeycloakId(), registrationRequest);
    }

    @Transactional
    public void createNewAccount(@Valid RegistrationRequest request) {
        keycloakUserService.registerUser(request);
    }

    public void deleteUser(String email) {
        accountsRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        String keycloakId = accountsRepository.findByEmail(email).get().getKeycloakId();
        keycloakUserService.deleteUser(keycloakId);
    }
}
