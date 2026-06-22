package cl.catastrofescl.notifications.repository;

import cl.catastrofescl.notifications.entity.PreferenciaNotificacion;
import cl.catastrofescl.notifications.entity.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PreferenciaNotificacionRepository extends JpaRepository<PreferenciaNotificacion, UUID> {

    List<PreferenciaNotificacion> findByUsuarioId(UUID usuarioId);

    Optional<PreferenciaNotificacion> findByUsuarioIdAndTipoEvento(UUID usuarioId, TipoNotificacion tipoEvento);
}
