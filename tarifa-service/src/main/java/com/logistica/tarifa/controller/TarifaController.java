package com.logistica.tarifa.controller;

import com.logistica.tarifa.model.TarifaRequest;
import com.logistica.tarifa.model.TarifaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tarifas")
public class TarifaController {

    @PostMapping("/calcular")
    public ResponseEntity<TarifaResponse> calcularTarifa(@RequestBody TarifaRequest request) {
        System.out.println("Calculando tarifa para peso: " + request.getPeso() + " y destino: " + request.getDestino());
        
        // Lógica simple de cálculo de tarifa
        double base = 10.0;
        double costo = base + (request.getPeso() * 2.5);
        if ("Internacional".equalsIgnoreCase(request.getDestino())) {
            costo += 50.0;
        }

        return ResponseEntity.ok(new TarifaResponse(costo));
    }
}
