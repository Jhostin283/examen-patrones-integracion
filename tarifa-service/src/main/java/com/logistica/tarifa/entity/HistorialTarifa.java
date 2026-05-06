package com.logistica.tarifa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "historial_tarifa")
public class HistorialTarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double pesoSolicitado;
    private String destino;
    private double costoCalculado;

    public HistorialTarifa() {}

    public HistorialTarifa(double pesoSolicitado, String destino, double costoCalculado) {
        this.pesoSolicitado = pesoSolicitado;
        this.destino = destino;
        this.costoCalculado = costoCalculado;
    }

    public Long getId() { return id; }
    public double getPesoSolicitado() { return pesoSolicitado; }
    public void setPesoSolicitado(double pesoSolicitado) { this.pesoSolicitado = pesoSolicitado; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public double getCostoCalculado() { return costoCalculado; }
    public void setCostoCalculado(double costoCalculado) { this.costoCalculado = costoCalculado; }
}