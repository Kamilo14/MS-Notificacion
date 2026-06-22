package cl.catastrofescl.notifications.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableRabbit
@Profile("!test")
public class RabbitMQConfig {

    public static final String DLQ_NOTIFICATIONS = "notifications.dlq";
    public static final String DLQ_EMAIL = "email.dlq";

    @Value("${catastrofescl.rabbitmq.exchange-topic}")
    private String exchangeTopic;

    @Value("${catastrofescl.rabbitmq.exchange-dlx}")
    private String exchangeDlx;

    @Value("${catastrofescl.rabbitmq.cola-notificaciones}")
    private String colaNotificaciones;

    @Value("${catastrofescl.rabbitmq.cola-email}")
    private String colaEmail;

    @Bean
    public TopicExchange catastrofesclEventsExchange() {
        return new TopicExchange(exchangeTopic, true, false);
    }

    @Bean
    public DirectExchange catastrofesclDlxExchange() {
        return new DirectExchange(exchangeDlx, true, false);
    }

    @Bean
    public Queue notificationsQueue() {
        return QueueBuilder.durable(colaNotificaciones)
                .withArgument("x-dead-letter-exchange", exchangeDlx)
                .withArgument("x-dead-letter-routing-key", DLQ_NOTIFICATIONS)
                .withArgument("x-max-retries", 3)
                .withArgument("x-message-ttl", 30000)
                .build();
    }

    @Bean
    public Queue notificationsDlq() {
        return QueueBuilder.durable(DLQ_NOTIFICATIONS).build();
    }

    @Bean
    public Binding notificationsDlqBinding(Queue notificationsDlq, DirectExchange catastrofesclDlxExchange) {
        return BindingBuilder.bind(notificationsDlq).to(catastrofesclDlxExchange).with(DLQ_NOTIFICATIONS);
    }

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(colaEmail)
                .withArgument("x-dead-letter-exchange", exchangeDlx)
                .withArgument("x-dead-letter-routing-key", DLQ_EMAIL)
                .withArgument("x-max-retries", 3)
                .withArgument("x-message-ttl", 30000)
                .build();
    }

    @Bean
    public Queue emailDlq() {
        return QueueBuilder.durable(DLQ_EMAIL).build();
    }

    @Bean
    public Binding emailDlqBinding(Queue emailDlq, DirectExchange catastrofesclDlxExchange) {
        return BindingBuilder.bind(emailDlq).to(catastrofesclDlxExchange).with(DLQ_EMAIL);
    }

    @Bean
    public MessageConverter messageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        template.setMandatory(true);
        return template;
    }
}
