package yers.dev.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Accounts extends BaseEntity{
    @Id
    @Column(name = "user_id")
    private long userId;

//
//    @Column(name="keycloak_id", unique = true, nullable = false)
//    private UUID keycloakId;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name="name")
    private String name;

    @Column(name="last_name")
    private String lastName;

    private boolean isActive;


    @Column(name="phone_number")
    private long phoneNumber;

}
