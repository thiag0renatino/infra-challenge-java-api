package com.fiap.challenge_api.service.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(Long id){
        super("Recurso não encontrado: " + id);
    }
}
