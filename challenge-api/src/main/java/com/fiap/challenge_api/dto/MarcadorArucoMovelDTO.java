package com.fiap.challenge_api.dto;

import com.fiap.challenge_api.model.Moto;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MarcadorArucoMovelDTO {

    private Long idMarcadorMovel;
    private String codigoAruco;
    private LocalDate dataInstalacao;
    private Long idMoto;

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

    public Long getIdMoto() {
        return idMoto;
    }

    public void setIdMoto(Long idMoto) {
        this.idMoto = idMoto;
    }
}
