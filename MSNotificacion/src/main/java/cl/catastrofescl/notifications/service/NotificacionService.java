package cl.catastrofescl.notifications.service;

import cl.catastrofescl.notifications.dto.response.NotificacionResponse;
import cl.catastrofescl.notifications.entity.CanalNotificacion;
import cl.catastrofescl.notifications.entity.Notificacion;
import cl.catastrofescl.notifications.entity.TipoNotificacion;
import cl.catastrofescl.notifications.exception.NotificacionNoEncontradaException;
import cl.catastrofescl.notifications.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final PublicadorWebSocketNotificacion publicadorWebSocket;

    @Transactional(readOnly = true)
    public Page<NotificacionResponse> listarDelUsuario(UUID usuarioId, Pageable pageable) {
        return notificacionRepository.findByUsuarioIdOrderByCreadaEnDesc(usuarioId, pageable)
                .map(this::aResponse);
    }

    @Transactional
    public NotificacionResponse marcarComoLeida(UUID usuarioId, UUID notificacionId) {
        Notificacion notificacion = notificacionRepository.findByIdAndUsuarioId(notificacionId, usuarioId)
                .orElseThrow(() -> new NotificacionNoEncontradaException(notificacionId));
        if (!notificacion.isLeida()) {
            notificacion.setLeida(true);
            notificacion.setLeidaEn(OffsetDateTime.now());
            notificacionRepository.save(notificacion);
        }
        return aResponse(notificacion);
    }

    @Transactional
    public Notificacion crearInApp(UUID usuarioId,
                                   TipoNotificacion tipo,
                                   String titulo,
                                   String cuerpo,
                                   Map<String, Object> metadata) {
        Notificacion notificacion = Notificacion.builder()
                .usuarioId(usuarioId)
                .tipo(tipo)
                .titulo(titulo)
                .cuerpo(cuerpo)
                .canal(CanalNotificacion.IN_APP)
                .leida(false)
                .metadata(metadata)
                .creadaEn(OffsetDateTime.now())
                .build();
        Notificacion guardada = notificacionRepository.save(notificacion);
        publicadorWebSocket.publicar(usuarioId, aResponse(guardada));
        return guardada;
    }

    private NotificacionResponse aResponse(Notificacion n) {
        return new NotificacionResponse(
                n.getId(),
                n.getTipo(),
                n.getTitulo(),
                n.getCuerpo(),
                n.getCanal(),
                n.isLeida(),
                n.getMetadata(),
                n.getCreadaEn(),
                n.getLeidaEn()
        );
    }
}
