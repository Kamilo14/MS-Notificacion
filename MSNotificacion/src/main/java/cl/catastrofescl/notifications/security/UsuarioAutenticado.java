package cl.catastrofescl.notifications.security;

import java.util.Set;
import java.util.UUID;

public record UsuarioAutenticado(
        String firebaseUid,
        UUID usuarioId,
        Set<String> roles
) {
}
