package com.fiap.challenge_api.enuns;

public enum TipoUsuario {
    ADMIN("admin"),
    USER("user");

    private String tipo;

    TipoUsuario(String tipo){
        this.tipo = tipo;
    }

    public String getTipo(){
        return tipo;
    }
}
