package com.logistica.envio.entity;
import jakarta.persistence.*;
@Entity
public class Envio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clienteId;
    private double peso;
    private String destino;
    private double costoFinal;
    // Getters/Setters omitidos para brevedad
}