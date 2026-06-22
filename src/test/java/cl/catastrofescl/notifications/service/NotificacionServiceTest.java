package cl.catastrofescl.notifications.service;

import cl.catastrofescl.notifications.dto.response.NotificacionResponse;
import cl.catastrofescl.notifications.entity.CanalNotificacion;
import cl.catastrofescl.notifications.entity.Notificacion;
import cl.catastrofescl.notifications.entity.TipoNotificacion;
import cl.catastrofescl.notifications.exception.NotificacionNoEncontradaException;
import cl.catastrofescl.notifications.repository.NotificacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private PublicadorWebSocketNotificacion publicadorWebSocket;

    @InjectMocks
    private NotificacionService notificacionService;

    @Test
    void listarDelUsuario_devuelvePagina() {
        UUID usuarioId = UUID.randomUUID();
        Notificacion notificacion = Notificacion.builder()
                .id(UUID.randomUUID())
                .usuarioId(usuarioId)
                .tipo(TipoNotificacion.DONACION_CONFIRMADA)
                .titulo("Donacion")
                .cuerpo("Cuerpo")
                .canal(CanalNotificacion.IN_APP)
                .leida(false)
                .creadaEn(OffsetDateTime.now())
                .build();
        when(notificacionRepository.findByUsuarioIdOrderByCreadaEnDesc(usuarioId, PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(List.of(notificacion)));

        var pagina = notificacionService.listarDelUsuario(usuarioId, PageRequest.of(0, 20));

        assertThat(pagina.getTotalElements()).isEqualTo(1);
        assertThat(pagina.getContent().getFirst().titulo()).isEqualTo("Donacion");
    }

    @Test
    void marcarComoLeida_actualizaEstado() {
        UUID usuarioId = UUID.randomUUID();
        UUID notificacionId = UUID.randomUUID();
        Notificacion notificacion = Notificacion.builder()
                .id(notificacionId)
                .usuarioId(usuarioId)
                .tipo(TipoNotificacion.TRANSFERENCIA_CREADA)
                .titulo("Transferencia")
                .cuerpo("Cuerpo")
                .canal(CanalNotificacion.IN_APP)
                .leida(false)
                .creadaEn(OffsetDateTime.now())
                .build();
        when(notificacionRepository.findByIdAndUsuarioId(notificacionId, usuarioId))
                .thenReturn(Optional.of(notificacion));
        when(notificacionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        NotificacionResponse response = notificacionService.marcarComoLeida(usuarioId, notificacionId);

        assertThat(response.leida()).isTrue();
        assertThat(notificacion.getLeidaEn()).isNotNull();
    }

    @Test
    void marcarComoLeida_lanzaSiNoExiste() {
        UUID usuarioId = UUID.randomUUID();
        UUID notificacionId = UUID.randomUUID();
        when(notificacionRepository.findByIdAndUsuarioId(notificacionId, usuarioId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificacionService.marcarComoLeida(usuarioId, notificacionId))
                .isInstanceOf(NotificacionNoEncontradaException.class);
    }

    @Test
    void crearInApp_persisteYPublicaWebSocket() {
        UUID usuarioId = UUID.randomUUID();
        when(notificacionRepository.save(any())).thenAnswer(inv -> {
            Notificacion n = inv.getArgument(0);
            n.setId(UUID.randomUUID());
            return n;
        });

        Notificacion creada = notificacionService.crearInApp(
                usuarioId,
                TipoNotificacion.ANUNCIO_PUBLICADO,
                "Titulo",
                "Cuerpo",
                null
        );

        assertThat(creada.getId()).isNotNull();
        verify(publicadorWebSocket).publicar(any(), any());
    }
}
