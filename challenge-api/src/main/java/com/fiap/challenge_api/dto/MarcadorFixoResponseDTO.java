package com.fiap.challenge_api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"idMarcadorArucoFixo", "codigoAruco", "xpos", "ypos","patio"})
public class MarcadorFixoResponseDTO extends RepresentationModel<MarcadorFixoResponseDTO> {

    private Long idMarcadorArucoFixo;
    private String codigoAruco;
    private Float xPos;
    private Float yPos;
    private PatioDTO patio;

    public Float getXPos() {
        return xPos;
    }

    public void setXPos(Float xPos) {
        this.xPos = xPos;
    }

    public Float getYPos() {
        return yPos;
    }

    public void setYPos(Float yPos) {
        this.yPos = yPos;
    }

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

    public PatioDTO getPatio() {
        return patio;
    }

    public void setPatio(PatioDTO patio) {
        this.patio = patio;
    }
}
