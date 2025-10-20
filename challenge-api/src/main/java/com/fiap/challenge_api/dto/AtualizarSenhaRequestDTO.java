package com.fiap.challenge_api.dto;

public class AtualizarSenhaRequestDTO {

    private String senhaAntiga;
    private String senhaNova;

    public AtualizarSenhaRequestDTO() {
    }

    public AtualizarSenhaRequestDTO(String senhaAntiga, String senhaNova) {
        this.senhaAntiga = senhaAntiga;
        this.senhaNova = senhaNova;
    }

    public String getSenhaAntiga() {
        return senhaAntiga;
    }

    public void setSenhaAntiga(String senhaAntiga) {
        this.senhaAntiga = senhaAntiga;
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }
}
