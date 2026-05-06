package com.logistica.tarifa.model;

public class TarifaResponse {
    private double costo;

    public TarifaResponse(double costo) {
        this.costo = costo;
    }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }
}
