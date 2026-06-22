package cl.catastrofescl.notifications.service;

import cl.catastrofescl.notifications.dto.response.NotificacionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublicadorWebSocketNotificacion {

    private final SimpMessagingTemplate messagingTemplate;

    public void publicar(UUID usuarioId, NotificacionResponse notificacion) {
        String destino = "/topic/notificaciones/" + usuarioId;
        messagingTemplate.convertAndSend(destino, notificacion);
        log.debug("Notificacion publicada por WebSocket destino={} id={}", destino, notificacion.id());
    }
}
