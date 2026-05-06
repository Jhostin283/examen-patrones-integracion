package com.logistica.envio.model;

public class EnvioRequest {
    private String clienteId;
    private double peso;
    private String destino;

    // Getters and Setters
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
}
