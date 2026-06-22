package cl.catastrofescl.notifications.service;

import cl.catastrofescl.notifications.dto.request.ActualizarPreferenciasRequest;
import cl.catastrofescl.notifications.dto.response.PreferenciaNotificacionResponse;
import cl.catastrofescl.notifications.entity.PreferenciaNotificacion;
import cl.catastrofescl.notifications.entity.TipoNotificacion;
import cl.catastrofescl.notifications.repository.PreferenciaNotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PreferenciaNotificacionService {

    private final PreferenciaNotificacionRepository preferenciaRepository;

    @Transactional(readOnly = true)
    public List<PreferenciaNotificacionResponse> obtenerPreferencias(UUID usuarioId) {
        Map<TipoNotificacion, PreferenciaNotificacion> guardadas = new EnumMap<>(TipoNotificacion.class);
        preferenciaRepository.findByUsuarioId(usuarioId)
                .forEach(p -> guardadas.put(p.getTipoEvento(), p));

        List<PreferenciaNotificacionResponse> resultado = new ArrayList<>();
        for (TipoNotificacion tipo : TipoNotificacion.values()) {
            PreferenciaNotificacion pref = guardadas.get(tipo);
            if (pref != null) {
                resultado.add(aResponse(pref));
            } else {
                resultado.add(new PreferenciaNotificacionResponse(null, tipo, true, true, false));
            }
        }
        return resultado;
    }

    @Transactional
    public List<PreferenciaNotificacionResponse> actualizarPreferencias(UUID usuarioId,
                                                                      ActualizarPreferenciasRequest solicitud) {
        List<PreferenciaNotificacionResponse> resultado = new ArrayList<>();
        for (ActualizarPreferenciasRequest.PreferenciaItemRequest item : solicitud.preferencias()) {
            PreferenciaNotificacion pref = preferenciaRepository
                    .findByUsuarioIdAndTipoEvento(usuarioId, item.tipoEvento())
                    .orElseGet(() -> PreferenciaNotificacion.builder()
                            .usuarioId(usuarioId)
                            .tipoEvento(item.tipoEvento())
                            .build());
            pref.setInApp(item.inApp());
            pref.setPush(item.push());
            pref.setCorreo(item.correo());
            resultado.add(aResponse(preferenciaRepository.save(pref)));
        }
        return resultado;
    }

    @Transactional(readOnly = true)
    public boolean permiteInApp(UUID usuarioId, TipoNotificacion tipo) {
        return preferenciaRepository.findByUsuarioIdAndTipoEvento(usuarioId, tipo)
                .map(PreferenciaNotificacion::isInApp)
                .orElse(true);
    }

    @Transactional(readOnly = true)
    public boolean permiteCorreo(UUID usuarioId, TipoNotificacion tipo) {
        return preferenciaRepository.findByUsuarioIdAndTipoEvento(usuarioId, tipo)
                .map(PreferenciaNotificacion::isCorreo)
                .orElse(false);
    }

    private PreferenciaNotificacionResponse aResponse(PreferenciaNotificacion p) {
        return new PreferenciaNotificacionResponse(
                p.getId(),
                p.getTipoEvento(),
                p.isInApp(),
                p.isPush(),
                p.isCorreo()
        );
    }
}
