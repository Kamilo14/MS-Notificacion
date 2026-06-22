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
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Consume las colas de eventos de logística declaradas por MS Logística
 * ({@code transfer.created.queue}, {@code transfer.status.changed.queue})
 * y persiste notificaciones in-app en la BD.
 */
@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class ConsumidorEventosLogistica {

    private final IdempotenciaService idempotenciaService;
    private final ProcesadorEventosNotificacion procesador;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${catastrofescl.rabbitmq.cola-transfer-created}")
    public void consumirTransferCreated(Message mensaje,
                                       Channel channel,
                                       @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag)
            throws IOException {
        procesarMensaje("transfer.created", mensaje, channel, deliveryTag);
    }

    @RabbitListener(queues = "${catastrofescl.rabbitmq.cola-transfer-status-changed}")
    public void consumirTransferStatusChanged(Message mensaje,
                                              Channel channel,
                                              @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag)
            throws IOException {
        procesarMensaje("transfer.status.changed", mensaje, channel, deliveryTag);
    }

    @RabbitListener(queues = "${catastrofescl.rabbitmq.cola-mission-assigned}")
    public void consumirMissionAssigned(Message mensaje,
                                       Channel channel,
                                       @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag)
            throws IOException {
        procesarMensaje("mission.assigned", mensaje, channel, deliveryTag);
    }

    private void procesarMensaje(String routingKey,
                                 Message mensaje,
                                 Channel channel,
                                 long deliveryTag) throws IOException {
        try {
            UUID eventoId = extraerEventoId(mensaje.getBody());

            if (idempotenciaService.yaProcesado(eventoId)) {
                log.info("Evento logística {} ya procesado, se ignora", eventoId);
                channel.basicAck(deliveryTag, false);
                return;
            }

            procesador.procesar(routingKey, mensaje.getBody());
            idempotenciaService.marcarComoProcesado(eventoId);
            channel.basicAck(deliveryTag, false);
            log.info("Evento logística {} procesado routingKey={}", eventoId, routingKey);
        } catch (Exception ex) {
            log.error("Error procesando mensaje de logística routingKey={}: {}", routingKey, ex.getMessage(), ex);
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
        throw new IllegalArgumentException("eventoId/eventId ausente en el mensaje de logística");
    }
}
