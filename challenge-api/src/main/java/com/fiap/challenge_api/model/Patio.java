package com.fiap.challenge_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Patio {

    @Id
    @SequenceGenerator(
            name = "moto_patio_gen",
            sequenceName = "SEQ_PATIO_CH",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moto_patio_gen")
    private Long idPatio;

    private String nome;

    @NotBlank
    private String localizacao;

    private String descricao;

    @OneToMany(mappedBy = "patio")
    private List<MarcadorFixo> marcadoresFixos;

    @OneToMany(mappedBy = "patio")
    private List<Posicao> posicoes;

    @OneToMany(mappedBy = "patio")
    private List<Usuario> usuarios;

    public Long getIdPatio() {
        return idPatio;
    }

    public void setIdPatio(Long idPatio) {
        this.idPatio = idPatio;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<MarcadorFixo> getMarcadoresFixos() {
        return marcadoresFixos;
    }

    public void setMarcadoresFixos(List<MarcadorFixo> marcadoresFixos) {
        this.marcadoresFixos = marcadoresFixos;
    }

    public List<Posicao> getPosicoes() {
        return posicoes;
    }

    public void setPosicoes(List<Posicao> posicoes) {
        this.posicoes = posicoes;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
