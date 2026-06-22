package cl.catastrofescl.notifications.dto.response;

import cl.catastrofescl.notifications.entity.TipoNotificacion;

import java.util.UUID;

public record PreferenciaNotificacionResponse(
        UUID id,
        TipoNotificacion tipoEvento,
        boolean inApp,
        boolean push,
        boolean correo
) {
}
