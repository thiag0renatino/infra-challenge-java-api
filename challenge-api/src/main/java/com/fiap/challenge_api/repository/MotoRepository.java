package com.fiap.challenge_api.repository;

import com.fiap.challenge_api.model.Moto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MotoRepository extends JpaRepository<Moto, Long> {

    List<Moto> findByPlacaStartsWithIgnoreCase(String placa);
    List<Moto> findByStatusIgnoreCase(String status);

    @Query("""
    SELECT DISTINCT m
    FROM Moto m
    JOIN Posicao p ON p.moto.id = m.id
    WHERE p.patio.id = :patioId
""")
    List<Moto> findMotosQuePassaramPorPatio(@Param("patioId") Long patioId);

}
