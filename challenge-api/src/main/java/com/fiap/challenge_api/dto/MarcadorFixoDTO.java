package com.fiap.challenge_api.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MarcadorFixoDTO {

    private Long idMarcadorArucoFixo;
    private String codigoAruco;
    private Float xPos;
    private Float yPos;
    private Long idPatio;

    public Long getIdMarcadorArucoFixo() {
        return idMarcadorArucoFixo;
    }

    public void setIdMarcadorArucoFixo(Long idMarcadorArucoFixo) {
        this.idMarcadorArucoFixo = idMarcadorArucoFixo;
    }

    public String getCodigoAruco() {
        return codigoAruco;
    }

    public void setCodigoAruco(String codigoAruco) {
        this.codigoAruco = codigoAruco;
    }

    public Float getXPos() {
        return xPos;
    }

    public void setXPos(Float xPos) {
        this.xPos = xPos;
    }

    public Long getIdPatio() {
        return idPatio;
    }

    public void setIdPatio(Long idPatio) {
        this.idPatio = idPatio;
    }

    public Float getYPos() {
        return yPos;
    }

    public void setYPos(Float yPos) {
        this.yPos = yPos;
    }
}
