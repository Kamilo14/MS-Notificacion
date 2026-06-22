package cl.catastrofescl.notifications.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "preferencias_notificacion",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_preferencias_usuario_tipo",
                columnNames = {"usuario_id", "tipo_evento"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenciaNotificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_evento", nullable = false, length = 50)
    private TipoNotificacion tipoEvento;

    @Column(name = "in_app", nullable = false)
    @Builder.Default
    private boolean inApp = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean push = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean correo = false;
}
