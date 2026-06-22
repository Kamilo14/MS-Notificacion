package cl.catastrofescl.notifications.dto.response;

import cl.catastrofescl.notifications.entity.CanalNotificacion;
import cl.catastrofescl.notifications.entity.TipoNotificacion;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record NotificacionResponse(
        UUID id,
        TipoNotificacion tipo,
        String titulo,
        String cuerpo,
        CanalNotificacion canal,
        boolean leida,
        Map<String, Object> metadata,
        OffsetDateTime creadaEn,
        OffsetDateTime leidaEn
) {
}
