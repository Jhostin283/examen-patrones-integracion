package com.logistica.tarifa.entity;
import jakarta.persistence.*;
@Entity
public class HistorialTarifa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double pesoSolicitado;
    private double costoCalculado;
    // Getters/Setters omitidos
}