package cl.catastrofescl.notifications.repository;

import cl.catastrofescl.notifications.entity.EventoProcesado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventoProcesadoRepository extends JpaRepository<EventoProcesado, UUID> {
}
