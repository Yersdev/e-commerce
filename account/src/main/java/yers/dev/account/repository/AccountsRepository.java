package yers.dev.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yers.dev.account.entity.Accounts;
import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    Optional<Accounts> findByUserId(Long userId);

    Optional<Accounts> findByEmail(String email);

    Optional<Accounts> findByKeycloakId(String keycloakId);
}
