package cl.catastrofescl.notifications.messaging;

import cl.catastrofescl.notifications.service.IdempotenciaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class ConsumidorEventosNotificacion {

    private final IdempotenciaService idempotenciaService;
    private final ProcesadorEventosNotificacion procesador;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${catastrofescl.rabbitmq.cola-notificaciones}")
    public void consumir(Message mensaje,
                         Channel channel,
                         @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                         @Header(value = AmqpHeaders.RECEIVED_ROUTING_KEY, required = false) String routingKey)
            throws IOException {
        try {
            String key = routingKey != null ? routingKey : "desconocido";
            UUID eventoId = extraerEventoId(mensaje.getBody());

            if (idempotenciaService.yaProcesado(eventoId)) {
                log.info("Evento {} ya procesado, se ignora", eventoId);
                channel.basicAck(deliveryTag, false);
                return;
            }

            procesador.procesar(key, mensaje.getBody());
            idempotenciaService.marcarComoProcesado(eventoId);
            channel.basicAck(deliveryTag, false);
            log.info("Evento {} procesado routingKey={}", eventoId, key);
        } catch (Exception ex) {
            log.error("Error procesando mensaje de notificaciones: {}", ex.getMessage(), ex);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    private UUID extraerEventoId(byte[] cuerpo) throws IOException {
        JsonNode nodo = objectMapper.readTree(cuerpo);
        if (nodo.hasNonNull("eventoId")) {
            return UUID.fromString(nodo.get("eventoId").asText());
        }
        if (nodo.hasNonNull("eventId")) {
            return UUID.fromString(nodo.get("eventId").asText());
        }
        throw new IllegalArgumentException("eventoId/eventId ausente en el mensaje");
    }
}
