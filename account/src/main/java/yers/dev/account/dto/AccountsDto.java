package yers.dev.account.dto;

import lombok.Data;

@Data
public class AccountsDto {
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private long phoneNumber;

    private boolean isActive;

}
