package cl.catastrofescl.notifications.messaging;

import cl.catastrofescl.notifications.entity.TipoNotificacion;
import cl.catastrofescl.notifications.service.NotificacionService;
import cl.catastrofescl.notifications.service.PreferenciaNotificacionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcesadorEventosNotificacionTest {

    @Mock
    private NotificacionService notificacionService;

    @Mock
    private PreferenciaNotificacionService preferenciaService;

    @Mock
    private PublicadorEmailQueue publicadorEmailQueue;

    private ObjectMapper objectMapper;

    @InjectMocks
    private ProcesadorEventosNotificacion procesador;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        procesador = new ProcesadorEventosNotificacion(
                objectMapper, notificacionService, preferenciaService, publicadorEmailQueue);
    }

    @Test
    void procesarDonationCreated_creaNotificacionInApp() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        UUID eventoId = UUID.randomUUID();
        String json = """
                {
                  "eventId": "%s",
                  "usuarioDonanteId": "%s",
                  "codigoQr": "QR-123",
                  "donadoEn": "2026-06-21T10:00:00Z"
                }
                """.formatted(eventoId, usuarioId);

        when(preferenciaService.permiteInApp(usuarioId, TipoNotificacion.DONACION_CONFIRMADA)).thenReturn(true);
        when(preferenciaService.permiteCorreo(usuarioId, TipoNotificacion.DONACION_CONFIRMADA)).thenReturn(true);

        procesador.procesar("donation.created", json.getBytes(StandardCharsets.UTF_8));

        verify(notificacionService).crearInApp(
                eq(usuarioId),
                eq(TipoNotificacion.DONACION_CONFIRMADA),
                any(),
                any(),
                any());
        verify(publicadorEmailQueue).publicar(any());
    }

    @Test
    void procesarStockCritical_sinUsuario_publicaSoloEmail() throws Exception {
        UUID eventoId = UUID.randomUUID();
        String json = """
                {
                  "eventId": "%s",
                  "centroId": "%s",
                  "itemId": "%s",
                  "nivelCriticidad": "CRITICO"
                }
                """.formatted(eventoId, UUID.randomUUID(), UUID.randomUUID());

        procesador.procesar("stock.critical", json.getBytes(StandardCharsets.UTF_8));

        verify(notificacionService, never()).crearInApp(any(), any(), any(), any(), any());
        verify(publicadorEmailQueue).publicar(any());
    }
}
