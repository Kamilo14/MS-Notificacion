package cl.catastrofescl.notifications.controller;

import cl.catastrofescl.notifications.dto.response.NotificacionResponse;
import cl.catastrofescl.notifications.service.ContextoUsuario;
import cl.catastrofescl.notifications.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
@Tag(name = "Notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar notificaciones del usuario autenticado (paginado)")
    public Page<NotificacionResponse> listar(
            @PageableDefault(size = 20) Pageable pageable) {
        return notificacionService.listarDelUsuario(ContextoUsuario.requerirUsuarioId(), pageable);
    }

    @PatchMapping("/{id}/leer")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Marcar notificacion como leida")
    public NotificacionResponse marcarLeida(@PathVariable UUID id) {
        return notificacionService.marcarComoLeida(ContextoUsuario.requerirUsuarioId(), id);
    }
}
