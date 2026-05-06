package com.logistica.envio.controller;

import com.logistica.common.event.EnvioCreadoEvent;
import com.logistica.envio.model.EnvioRequest;
import com.logistica.envio.service.EnvioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @PostMapping
    public ResponseEntity<EnvioCreadoEvent> crearEnvio(@RequestBody EnvioRequest request) {
        EnvioCreadoEvent result = envioService.procesarEnvio(request);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
