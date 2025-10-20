package com.fiap.challenge_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Moto {

    @Id
    @SequenceGenerator(
            name = "moto_seq_gen",
            sequenceName = "SEQ_MOTO_CH",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moto_seq_gen")
    private Long idMoto;

    private String placa;

    private String modelo;

    private String status;

    @NotNull
    private LocalDate dataCadastro;

    @OneToMany(mappedBy = "moto")
    private List<Posicao> posicoes;

    @OneToMany(mappedBy = "moto")
    private List<MarcadorArucoMovel> marcadoresMoveis;

    public Long getIdMoto() {
        return idMoto;
    }

    public void setIdMoto(Long idMoto) {
        this.idMoto = idMoto;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<Posicao> getPosicoes() {
        return posicoes;
    }

    public void setPosicoes(List<Posicao> posicoes) {
        this.posicoes = posicoes;
    }

    public List<MarcadorArucoMovel> getMarcadoresMoveis() {
        return marcadoresMoveis;
    }

    public void setMarcadoresMoveis(List<MarcadorArucoMovel> marcadoresMoveis) {
        this.marcadoresMoveis = marcadoresMoveis;
    }
}
