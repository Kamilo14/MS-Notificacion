package cl.catastrofescl.notifications.repository;

import cl.catastrofescl.notifications.entity.Notificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NotificacionRepository extends JpaRepository<Notificacion, UUID> {

    Page<Notificacion> findByUsuarioIdOrderByCreadaEnDesc(UUID usuarioId, Pageable pageable);

    Optional<Notificacion> findByIdAndUsuarioId(UUID id, UUID usuarioId);
}
