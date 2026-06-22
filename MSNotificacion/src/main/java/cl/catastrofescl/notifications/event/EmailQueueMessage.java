package cl.catastrofescl.notifications.event;

import cl.catastrofescl.notifications.entity.TipoNotificacion;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public record EmailQueueMessage(
        UUID eventoId,
        String routingKey,
        TipoNotificacion tipo,
        UUID usuarioId,
        String titulo,
        String cuerpo,
        Map<String, Object> metadata,
        OffsetDateTime ocurridoEn
) {
}
