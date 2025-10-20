package com.fiap.challenge_api.service.exception;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String email) {
        super("Usuário com Email: " + email + " não encontrado");
    }
}
