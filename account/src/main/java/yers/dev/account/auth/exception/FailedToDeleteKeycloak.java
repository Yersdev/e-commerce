package yers.dev.account.auth.exception;

public class FailedToDeleteKeycloak extends RuntimeException {
    public FailedToDeleteKeycloak(String message) {
        super(String.format("Failed to delete user from Keycloak: %s", message));
    }
}
