package com.logistica.envio.service;

import com.logistica.envio.config.RabbitMQConfig;
import com.logistica.common.event.EnvioCreadoEvent;
import com.logistica.envio.entity.Envio;
import com.logistica.envio.model.EnvioRequest;
import com.logistica.envio.model.TarifaResponse;
import com.logistica.envio.repository.EnvioRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EnvioService {

    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final EnvioRepository envioRepository;

    @Value("${tarifa.service.url}")
    private String tarifaServiceUrl;

    public EnvioService(RestTemplate restTemplate, RabbitTemplate rabbitTemplate, EnvioRepository envioRepository) {
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
        this.envioRepository = envioRepository;
    }

    public EnvioCreadoEvent procesarEnvio(EnvioRequest request) {
        // 1. Orquestación Síncrona: Llamar al servicio de tarifas para obtener el costo
        String url = tarifaServiceUrl + "/calcular";
        ResponseEntity<TarifaResponse> response = restTemplate.postForEntity(url, request, TarifaResponse.class);
        double costo = response.getBody() != null ? response.getBody().getCosto() : 0.0;

        // 2. Persistencia en Base de Datos propia (Database per Service)
        Envio envio = new Envio(request.getClienteId(), request.getPeso(), request.getDestino(), costo);
        envioRepository.save(envio);
        System.out.println("Envío guardado en BD con ID: " + envio.getId());

        // 3. Coreografía Asíncrona: Publicar el evento para el servicio de notificaciones
        EnvioCreadoEvent evento = new EnvioCreadoEvent(
            request.getClienteId(), request.getPeso(), request.getDestino(), costo
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, evento);
        System.out.println("Evento publicado en RabbitMQ para cliente: " + request.getClienteId());

        return evento;
    }
}
