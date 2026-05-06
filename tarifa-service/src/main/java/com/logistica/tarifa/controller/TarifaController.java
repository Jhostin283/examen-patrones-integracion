package com.logistica.tarifa.controller;

import com.logistica.tarifa.entity.HistorialTarifa;
import com.logistica.tarifa.model.TarifaRequest;
import com.logistica.tarifa.model.TarifaResponse;
import com.logistica.tarifa.repository.HistorialTarifaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tarifas")
public class TarifaController {

    private final HistorialTarifaRepository historialRepository;

    public TarifaController(HistorialTarifaRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    @PostMapping("/calcular")
    public ResponseEntity<TarifaResponse> calcularTarifa(@RequestBody TarifaRequest request) {
        System.out.println("Calculando tarifa para peso: " + request.getPeso() + " y destino: " + request.getDestino());

        // Lógica de cálculo de tarifa
        double base = 10.0;
        double costo = base + (request.getPeso() * 2.5);
        if ("Internacional".equalsIgnoreCase(request.getDestino())) {
            costo += 50.0;
        }

        // Persistencia del historial en su propia BD (Database per Service)
        HistorialTarifa historial = new HistorialTarifa(request.getPeso(), request.getDestino(), costo);
        historialRepository.save(historial);
        System.out.println("Historial de tarifa guardado en BD con ID: " + historial.getId());

        return ResponseEntity.ok(new TarifaResponse(costo));
    }
}
