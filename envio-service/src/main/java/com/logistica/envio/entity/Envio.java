package com.logistica.envio.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "envio")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clienteId;
    private double peso;
    private String destino;
    private double costoFinal;

    public Envio() {}

    public Envio(String clienteId, double peso, String destino, double costoFinal) {
        this.clienteId = clienteId;
        this.peso = peso;
        this.destino = destino;
        this.costoFinal = costoFinal;
    }

    public Long getId() { return id; }
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public double getCostoFinal() { return costoFinal; }
    public void setCostoFinal(double costoFinal) { this.costoFinal = costoFinal; }
}