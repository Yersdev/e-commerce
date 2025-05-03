package yers.dev.account.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Accounts extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

//
//    @Column(name="keycloak_id", unique = true, nullable = false)
//    private UUID keycloakId;
    /** Уникальный идентификатор пользователя в Keycloak */
    @Column(unique = true, nullable = false)
    private String keycloakId;


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
