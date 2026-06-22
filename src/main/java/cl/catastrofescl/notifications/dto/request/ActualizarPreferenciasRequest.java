package cl.catastrofescl.notifications.dto.request;

import cl.catastrofescl.notifications.entity.TipoNotificacion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ActualizarPreferenciasRequest(
        @NotEmpty List<@Valid PreferenciaItemRequest> preferencias
) {
    public record PreferenciaItemRequest(
            @NotNull TipoNotificacion tipoEvento,
            boolean inApp,
            boolean push,
            boolean correo
    ) {
    }
}
