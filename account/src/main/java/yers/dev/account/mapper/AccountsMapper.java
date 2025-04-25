    package yers.dev.account.mapper;

    import org.springframework.stereotype.Component;
    import org.springframework.stereotype.Controller;
    import yers.dev.account.dto.AccountsDto;
    import yers.dev.account.entity.Accounts;

    import java.util.List;
    import java.util.stream.Collectors;

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

        public static List<AccountsDto> mapToUserDto(List<Accounts> users) {
            return users.stream().map(user -> mapToUserDto(user, new AccountsDto())).collect(Collectors.toList());
        }
    }
