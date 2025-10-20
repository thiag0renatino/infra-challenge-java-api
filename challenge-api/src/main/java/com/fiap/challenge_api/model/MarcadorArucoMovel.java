package com.fiap.challenge_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MarcadorArucoMovel {

    @Id
    @SequenceGenerator(
            name = "marcador_movel_seq_gen",
            sequenceName = "SEQ_MARCADOR_M_CH",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "marcador_movel_seq_gen")
    private Long idMarcadorMovel;

    private String codigoAruco;

    @NotNull
    private LocalDate dataInstalacao;

    @ManyToOne
    @JoinColumn(name = "moto_id_moto")
    private Moto moto;

    public Long getIdMarcadorMovel() {
        return idMarcadorMovel;
    }

    public void setIdMarcadorMovel(Long idMarcadorMovel) {
        this.idMarcadorMovel = idMarcadorMovel;
    }

    public String getCodigoAruco() {
        return codigoAruco;
    }

    public void setCodigoAruco(String codigoAruco) {
        this.codigoAruco = codigoAruco;
    }

    public LocalDate getDataInstalacao() {
        return dataInstalacao;
    }

    public void setDataInstalacao(LocalDate dataInstalacao) {
        this.dataInstalacao = dataInstalacao;
    }

    public Moto getMoto() {
        return moto;
    }

    public void setMoto(Moto moto) {
        this.moto = moto;
    }
}
