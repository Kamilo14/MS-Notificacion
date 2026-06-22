package cl.catastrofescl.notifications.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "notificaciones_eventos_procesados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoProcesado {

    @Id
    @Column(name = "evento_id")
    private UUID eventoId;

    @Column(nullable = false, length = 50)
    private String consumidor;

    @Column(name = "procesado_en", nullable = false)
    private OffsetDateTime procesadoEn;
}
