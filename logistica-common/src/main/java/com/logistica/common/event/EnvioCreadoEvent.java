package com.logistica.common.event;

public class EnvioCreadoEvent {
    private String clienteId;
    private double peso;
    private String destino;
    private double costoFinal;

    public EnvioCreadoEvent() {}

    public EnvioCreadoEvent(String clienteId, double peso, String destino, double costoFinal) {
        this.clienteId = clienteId;
        this.peso = peso;
        this.destino = destino;
        this.costoFinal = costoFinal;
    }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public double getCostoFinal() { return costoFinal; }
    public void setCostoFinal(double costoFinal) { this.costoFinal = costoFinal; }
}