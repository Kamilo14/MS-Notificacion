package cl.catastrofescl.notifications.controller;

import cl.catastrofescl.notifications.dto.request.ActualizarPreferenciasRequest;
import cl.catastrofescl.notifications.dto.response.PreferenciaNotificacionResponse;
import cl.catastrofescl.notifications.service.ContextoUsuario;
import cl.catastrofescl.notifications.service.PreferenciaNotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones/preferencias")
@RequiredArgsConstructor
@Tag(name = "Preferencias de notificacion")
public class PreferenciaNotificacionController {

    private final PreferenciaNotificacionService preferenciaService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener preferencias de notificacion del usuario")
    public List<PreferenciaNotificacionResponse> obtener() {
        return preferenciaService.obtenerPreferencias(ContextoUsuario.requerirUsuarioId());
    }

    @PatchMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Actualizar preferencias de notificacion")
    public List<PreferenciaNotificacionResponse> actualizar(
            @Valid @RequestBody ActualizarPreferenciasRequest solicitud) {
        return preferenciaService.actualizarPreferencias(ContextoUsuario.requerirUsuarioId(), solicitud);
    }
}
