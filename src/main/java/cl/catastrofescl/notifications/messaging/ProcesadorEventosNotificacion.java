package cl.catastrofescl.notifications.messaging;

import cl.catastrofescl.notifications.entity.TipoNotificacion;
import cl.catastrofescl.notifications.event.EmailQueueMessage;
import cl.catastrofescl.notifications.service.NotificacionService;
import cl.catastrofescl.notifications.service.PreferenciaNotificacionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcesadorEventosNotificacion {

    private static final Set<String> ROUTING_KEYS_EMAIL = Set.of(
            "donation.created",
            "donation.confirmed",
            "stock.critical",
            "transfer.created",
            "transfer.status.changed",
            "mission.assigned",
            "announcement.published"
    );

    private final ObjectMapper objectMapper;
    private final NotificacionService notificacionService;
    private final PreferenciaNotificacionService preferenciaService;
    private final PublicadorEmailQueue publicadorEmailQueue;

    public void procesar(String routingKey, byte[] cuerpoMensaje) {
        JsonNode nodo = parsear(cuerpoMensaje);
        UUID eventoId = extraerEventoId(nodo);
        TipoNotificacion tipo = mapearTipo(routingKey);
        Optional<UUID> usuarioDestino = resolverUsuarioDestino(routingKey, nodo);

        if (usuarioDestino.isEmpty()) {
            log.info("Evento {} sin destinatario resoluble, se omite notificacion in-app", routingKey);
            if (ROUTING_KEYS_EMAIL.contains(routingKey)) {
                publicarEmailSiAplica(routingKey, tipo, null, eventoId, nodo, tituloPorDefecto(tipo, nodo),
                        cuerpoPorDefecto(tipo, nodo), metadataDesde(nodo));
            }
            return;
        }

        UUID usuarioId = usuarioDestino.get();
        String titulo = tituloPorDefecto(tipo, nodo);
        String cuerpo = cuerpoPorDefecto(tipo, nodo);
        Map<String, Object> metadata = metadataDesde(nodo);

        if (preferenciaService.permiteInApp(usuarioId, tipo)) {
            notificacionService.crearInApp(usuarioId, tipo, titulo, cuerpo, metadata);
        }

        if (ROUTING_KEYS_EMAIL.contains(routingKey)) {
            publicarEmailSiAplica(routingKey, tipo, usuarioId, eventoId, nodo, titulo, cuerpo, metadata);
        }
    }

    private void publicarEmailSiAplica(String routingKey,
                                       TipoNotificacion tipo,
                                       UUID usuarioId,
                                       UUID eventoId,
                                       JsonNode nodo,
                                       String titulo,
                                       String cuerpo,
                                       Map<String, Object> metadata) {
        if (usuarioId != null && !preferenciaService.permiteCorreo(usuarioId, tipo)) {
            return;
        }
        OffsetDateTime ocurridoEn = extraerFecha(nodo);
        publicadorEmailQueue.publicar(new EmailQueueMessage(
                eventoId,
                routingKey,
                tipo,
                usuarioId,
                titulo,
                cuerpo,
                metadata,
                ocurridoEn
        ));
    }

    private JsonNode parsear(byte[] cuerpoMensaje) {
        try {
            return objectMapper.readTree(cuerpoMensaje);
        } catch (Exception ex) {
            throw new IllegalArgumentException("No se pudo parsear el cuerpo del evento", ex);
        }
    }

    private UUID extraerEventoId(JsonNode nodo) {
        UUID id = leerUuid(nodo, "eventoId");
        if (id != null) {
            return id;
        }
        id = leerUuid(nodo, "eventId");
        if (id != null) {
            return id;
        }
        throw new IllegalArgumentException("El evento no incluye eventoId ni eventId");
    }

    private OffsetDateTime extraerFecha(JsonNode nodo) {
        if (nodo.hasNonNull("ocurridoEn")) {
            return OffsetDateTime.parse(nodo.get("ocurridoEn").asText());
        }
        if (nodo.hasNonNull("donadoEn")) {
            return OffsetDateTime.parse(nodo.get("donadoEn").asText());
        }
        return OffsetDateTime.now();
    }

    private Optional<UUID> resolverUsuarioDestino(String routingKey, JsonNode nodo) {
        return switch (routingKey) {
            case "donation.created", "donation.confirmed" ->
                    Optional.ofNullable(leerUuid(nodo, "usuarioDonanteId"));
            case "transfer.created" ->
                    Optional.ofNullable(leerUuid(nodo, "solicitadoPorUsuarioId"));
            case "transfer.status.changed" ->
                    Optional.ofNullable(leerUuid(nodo, "solicitadoPorUsuarioId"));
            case "mission.assigned" ->
                    Optional.ofNullable(primeroNoNulo(
                            leerUuid(nodo, "voluntarioUsuarioId"),
                            leerUuid(nodo, "usuarioVoluntarioId"),
                            leerUuid(nodo, "asignadoAUsuarioId")));
            case "emergency.created" ->
                    Optional.ofNullable(leerUuid(nodo, "declaradaPorUsuarioId"));
            case "announcement.published" ->
                    Optional.ofNullable(leerUuid(nodo, "autorUsuarioId"));
            case "need.created" ->
                    Optional.ofNullable(leerUuid(nodo, "usuarioId"));
            default -> Optional.empty();
        };
    }

    private TipoNotificacion mapearTipo(String routingKey) {
        return switch (routingKey) {
            case "stock.critical" -> TipoNotificacion.STOCK_CRITICO;
            case "donation.created", "donation.confirmed" -> TipoNotificacion.DONACION_CONFIRMADA;
            case "transfer.created" -> TipoNotificacion.TRANSFERENCIA_CREADA;
            case "transfer.status.changed" -> TipoNotificacion.TRANSFERENCIA_ESTADO;
            case "mission.assigned" -> TipoNotificacion.MISION_ASIGNADA;
            case "emergency.created" -> TipoNotificacion.EMERGENCIA_CREADA;
            case "announcement.published" -> TipoNotificacion.ANUNCIO_PUBLICADO;
            case "need.created" -> TipoNotificacion.NECESIDAD_CREADA;
            default -> throw new IllegalArgumentException("Routing key no soportada: " + routingKey);
        };
    }

    private String tituloPorDefecto(TipoNotificacion tipo, JsonNode nodo) {
        if (nodo.hasNonNull("titulo")) {
            return nodo.get("titulo").asText();
        }
        return switch (tipo) {
            case STOCK_CRITICO -> "Stock critico detectado";
            case DONACION_CONFIRMADA -> "Donacion registrada";
            case TRANSFERENCIA_CREADA -> "Nueva transferencia solicitada";
            case TRANSFERENCIA_ESTADO -> "Actualizacion de transferencia";
            case MISION_ASIGNADA -> "Mision asignada";
            case EMERGENCIA_CREADA -> "Emergencia declarada";
            case ANUNCIO_PUBLICADO -> "Nuevo anuncio publicado";
            case NECESIDAD_CREADA -> "Nueva necesidad registrada";
        };
    }

    private String cuerpoPorDefecto(TipoNotificacion tipo, JsonNode nodo) {
        if (nodo.hasNonNull("contenido")) {
            return nodo.get("contenido").asText();
        }
        if (nodo.hasNonNull("resumen")) {
            return nodo.get("resumen").asText();
        }
        if (nodo.hasNonNull("codigoQr")) {
            return "Codigo QR de donacion: " + nodo.get("codigoQr").asText();
        }
        return "Evento " + tipo.name() + " recibido en el sistema.";
    }

    private Map<String, Object> metadataDesde(JsonNode nodo) {
        Map<String, Object> metadata = new HashMap<>();
        nodo.fields().forEachRemaining(entry ->
                metadata.put(entry.getKey(), objectMapper.convertValue(entry.getValue(), Object.class)));
        return metadata;
    }

    private UUID leerUuid(JsonNode nodo, String campo) {
        if (!nodo.hasNonNull(campo)) {
            return null;
        }
        try {
            return UUID.fromString(nodo.get(campo).asText());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    @SafeVarargs
    private UUID primeroNoNulo(UUID... valores) {
        for (UUID valor : valores) {
            if (valor != null) {
                return valor;
            }
        }
        return null;
    }
}
