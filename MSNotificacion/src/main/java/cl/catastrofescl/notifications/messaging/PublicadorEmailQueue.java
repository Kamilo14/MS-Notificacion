package cl.catastrofescl.notifications.messaging;

import cl.catastrofescl.notifications.event.EmailQueueMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublicadorEmailQueue {

    private final RabbitTemplate rabbitTemplate;

    @Value("${catastrofescl.rabbitmq.cola-email:email.queue}")
    private String colaEmail;

    public void publicar(EmailQueueMessage mensaje) {
        rabbitTemplate.convertAndSend(colaEmail, mensaje);
        log.debug("Mensaje encolado en {} eventoId={} routingKey={}",
                colaEmail, mensaje.eventoId(), mensaje.routingKey());
    }
}
