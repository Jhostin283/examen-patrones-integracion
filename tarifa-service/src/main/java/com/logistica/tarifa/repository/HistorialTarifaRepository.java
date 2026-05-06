package com.logistica.tarifa.repository;

import com.logistica.tarifa.entity.HistorialTarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialTarifaRepository extends JpaRepository<HistorialTarifa, Long> {
}
