package yers.dev.account.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import yers.dev.account.dto.AccountsDto;
import yers.dev.account.entity.Accounts;
import yers.dev.account.mapper.AccountsMapper;
import yers.dev.account.repository.AccountsRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AccountsService {
    private AccountsRepository accountsRepository;

    public void deleteUserById(AccountsDto accountsDto) {
        accountsRepository.findByUserId(accountsRepository.findByEmail(accountsDto.getEmail()).orElseThrow(() -> new RuntimeException("User not found")).getUserId());
    }

    public AccountsDto getUserById(Long userId) {
        Accounts user = accountsRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return AccountsMapper.mapToUserDto(user, new AccountsDto());
    }

    @Transactional
    public boolean updateUser(AccountsDto accountsDto) {
        Accounts user = accountsRepository.findByEmail(accountsDto.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        AccountsMapper.mapToUser(user, accountsDto);
        Accounts updatedUser = accountsRepository.save(user);
        return true;
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

}
