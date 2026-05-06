package com.logistica.notificacion.listener;

import com.logistica.common.event.EnvioCreadoEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacionListener {

    @RabbitListener(queues = "notificacion.queue")
    public void procesarNotificacion(EnvioCreadoEvent evento) {
        System.out.println("=================================================");
        System.out.println("NUEVA NOTIFICACIÓN RECIBIDA (Asíncrona)");
        System.out.println("Cliente: " + evento.getClienteId());
        System.out.println("Destino: " + evento.getDestino());
        System.out.println("Costo a pagar: $" + evento.getCostoFinal());
        System.out.println("=================================================");
    }
}
