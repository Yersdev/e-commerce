package yers.dev.account.mapper;

import yers.dev.account.auth.entity.dto.AuthRequest;
import yers.dev.account.auth.entity.dto.RegistrationRequest;

public class AuthMapper {

    // RegistrationRequest → AuthRequest
    public static AuthRequest toAuthRequest(RegistrationRequest reg) {
        AuthRequest auth = new AuthRequest();
        auth.setEmail(reg.getEmail());
        auth.setPassword(reg.getPassword());
        return auth;
    }

    // AuthRequest → RegistrationRequest (имя и фамилия — пустые, телефон тоже)
    public static RegistrationRequest toRegistrationRequest(AuthRequest auth) {
        RegistrationRequest reg = new RegistrationRequest();
        reg.setEmail(auth.getEmail());
        reg.setPassword(auth.getPassword());
        reg.setFirstName("");
        reg.setLastName("");
        reg.setPhoneNumber("");
        return reg;
    }
}
