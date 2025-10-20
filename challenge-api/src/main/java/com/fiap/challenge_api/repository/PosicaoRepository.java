package com.fiap.challenge_api.repository;

import com.fiap.challenge_api.model.Posicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PosicaoRepository extends JpaRepository<Posicao, Long> {

    List<Posicao> findByMoto_IdMoto(Long motoId);

    List<Posicao> findTop10ByOrderByDataHoraDesc();

    @Query("SELECT p FROM Posicao p WHERE p.moto.idMoto = :motoId ORDER BY p.dataHora DESC")
    List<Posicao> buscarHistoricoDaMoto(@Param("motoId") Long motoId);

    @Query("SELECT p FROM Posicao p WHERE p.moto.status = 'Revisão'")
    List<Posicao> findPosicoesDeMotosRevisao();

    List<Posicao> findByPatioIdPatio(Long patioId);
}
