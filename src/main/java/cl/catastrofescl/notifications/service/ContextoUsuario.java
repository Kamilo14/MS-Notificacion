package cl.catastrofescl.notifications.service;

import cl.catastrofescl.notifications.security.UsuarioAutenticado;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class ContextoUsuario {

    private ContextoUsuario() {
    }

    public static UsuarioAutenticado requerirPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UsuarioAutenticado principal)) {
            throw new IllegalStateException("No hay usuario autenticado en el contexto de seguridad");
        }
        return principal;
    }

    public static UUID requerirUsuarioId() {
        return requerirPrincipal().usuarioId();
    }
}
