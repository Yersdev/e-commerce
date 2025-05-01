package yers.dev.account.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность пользователя, представляющая данные из локальной базы данных,
 * синхронизированные с пользователем из Keycloak.
 */
@Entity
@Getter
@Setter
public class Users {

    /** Уникальный идентификатор пользователя в базе данных */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Уникальный идентификатор пользователя в Keycloak */
    @Column(unique = true, nullable = false)
    private String keycloakId;

    /** Имя пользователя */
    private String firstName;

    /** Фамилия пользователя */
    private String lastName;

    /** Email пользователя */
    private String email;
}
