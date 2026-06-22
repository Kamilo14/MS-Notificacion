package cl.catastrofescl.notifications.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class NotificacionNoEncontradaException extends RuntimeException {

    private final UUID notificacionId;

    public NotificacionNoEncontradaException(UUID notificacionId) {
        super("Notificacion no encontrada: " + notificacionId);
        this.notificacionId = notificacionId;
    }
}
