package com.fiap.challenge_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Posicao {

    @Id
    @SequenceGenerator(
            name = "moto_pos_gen",
            sequenceName = "SEQ_POS_CH",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moto_pos_gen")
    private Long idPosicao;

    private Float xPos;

    private Float yPos;

    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "moto_id_moto")
    private Moto moto;

    @ManyToOne
    @JoinColumn(name = "patio_id_patio")
    private Patio patio;

    @OneToMany(mappedBy = "posicao")
    private List<MedicaoPosicao> medicoes;

    public Long getIdPosicao() {
        return idPosicao;
    }

    public void setIdPosicao(Long idPosicao) {
        this.idPosicao = idPosicao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Moto getMoto() {
        return moto;
    }

    public void setMoto(Moto moto) {
        this.moto = moto;
    }

    public Patio getPatio() {
        return patio;
    }

    public void setPatio(Patio patio) {
        this.patio = patio;
    }

    public List<MedicaoPosicao> getMedicoes() {
        return medicoes;
    }

    public void setMedicoes(List<MedicaoPosicao> medicoes) {
        this.medicoes = medicoes;
    }

    public Float getYPos() {
        return yPos;
    }

    public void setYPos(Float yPos) {
        this.yPos = yPos;
    }

    public Float getXPos() {
        return xPos;
    }

    public void setXPos(Float xPos) {
        this.xPos = xPos;
    }
}
