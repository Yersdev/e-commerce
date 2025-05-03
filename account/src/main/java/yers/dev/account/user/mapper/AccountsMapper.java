    package yers.dev.account.user.mapper;

    import yers.dev.account.user.entity.dto.AccountsDto;
    import yers.dev.account.user.entity.Accounts;

    import java.util.List;
    import java.util.stream.Collectors;

    public class AccountsMapper {
        public static AccountsDto mapToUserDto(Accounts user, AccountsDto accountsDto) {
            accountsDto.setId(user.getUserId());
            accountsDto.setFirstName(user.getName());
            accountsDto.setLastName(user.getLastName());
            accountsDto.setEmail(user.getEmail());
            accountsDto.setPhoneNumber(user.getPhoneNumber());
            accountsDto.setActive(user.isActive());
            return accountsDto;
        }

        public static Accounts mapToUser(Accounts user, AccountsDto accountsDto) {
            user.setUserId(accountsDto.getId());
            user.setName(accountsDto.getFirstName());
            user.setLastName(accountsDto.getLastName());
            user.setEmail(accountsDto.getEmail());
            user.setPhoneNumber(accountsDto.getPhoneNumber());
            user.setActive(accountsDto.isActive());
            return user;
        }

        public static List<AccountsDto> mapToUserDto(List<Accounts> users) {
            return users.stream().map(user -> mapToUserDto(user, new AccountsDto())).collect(Collectors.toList());
        }
    }
