package cl.catastrofescl.notifications.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

/**
 * Enlaza {@code notifications.queue} a eventos de dominio distintos de logística.
 * Los eventos de transferencia y misión se consumen desde las colas de MS Logística.
 */
@Configuration
@Profile("!test")
public class BindingNotificacionesConfig {

    private static final String[] ROUTING_KEYS = {
            "donation.created",
            "donation.confirmed",
            "stock.critical",
            "emergency.created",
            "announcement.published",
            "need.created"
    };

    @Bean
    public Declarables bindingsColaNotificaciones(Queue notificationsQueue,
                                                  TopicExchange catastrofesclEventsExchange) {
        List<Binding> bindings = Arrays.stream(ROUTING_KEYS)
                .map(key -> BindingBuilder.bind(notificationsQueue)
                        .to(catastrofesclEventsExchange)
                        .with(key))
                .toList();
        return new Declarables(bindings);
    }
}
