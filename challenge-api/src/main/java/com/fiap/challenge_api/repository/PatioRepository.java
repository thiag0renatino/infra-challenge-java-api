package com.fiap.challenge_api.repository;

import com.fiap.challenge_api.model.Patio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatioRepository extends JpaRepository<Patio, Long> {

    @Query("SELECT DISTINCT p FROM Patio p JOIN p.posicoes ps WHERE ps.moto IS NOT NULL")
    List<Patio> findPatiosComMotos();

}
