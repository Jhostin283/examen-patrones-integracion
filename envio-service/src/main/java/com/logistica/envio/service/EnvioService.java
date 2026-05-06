package com.logistica.envio.service;

import com.logistica.envio.config.RabbitMQConfig;
import com.logistica.common.event.EnvioCreadoEvent;
import com.logistica.envio.model.EnvioRequest;
import com.logistica.envio.model.TarifaResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EnvioService {

    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${tarifa.service.url}")
    private String tarifaServiceUrl;

    public EnvioService(RestTemplate restTemplate, RabbitTemplate rabbitTemplate) {
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    public EnvioCreadoEvent procesarEnvio(EnvioRequest request) {
        // 1. Orquestación Síncrona: Llamar al servicio de tarifas para obtener el costo
        String url = tarifaServiceUrl + "/calcular";
        ResponseEntity<TarifaResponse> response = restTemplate.postForEntity(url, request, TarifaResponse.class);
        
        double costo = response.getBody() != null ? response.getBody().getCosto() : 0.0;

        // Crear el evento
        EnvioCreadoEvent evento = new EnvioCreadoEvent(
            request.getClienteId(), request.getPeso(), request.getDestino(), costo
        );

        // 2. Coreografía Asíncrona: Publicar el evento para el servicio de notificaciones
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, evento);
        
        System.out.println("Envío procesado y evento publicado para cliente: " + request.getClienteId());
        
        return evento;
    }
}
