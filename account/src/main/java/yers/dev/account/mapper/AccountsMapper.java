package yers.dev.account.mapper;

import yers.dev.account.dto.AccountsDto;
import yers.dev.account.entity.Accounts;

public class AccountsMapper {
    public static AccountsDto mapToUserDto(Accounts user, AccountsDto accountsDto) {
        accountsDto.setName(user.getName());
        accountsDto.setLastName(user.getLastName());
        accountsDto.setEmail(user.getEmail());
        accountsDto.setPhoneNumber(user.getPhoneNumber());
        return accountsDto;
    }

    public static Accounts mapToUser(Accounts user, AccountsDto accountsDto) {
        user.setName(accountsDto.getName());
        user.setLastName(accountsDto.getLastName());
        user.setEmail(accountsDto.getEmail());
        user.setPhoneNumber(accountsDto.getPhoneNumber());
        return user;
    }
}
