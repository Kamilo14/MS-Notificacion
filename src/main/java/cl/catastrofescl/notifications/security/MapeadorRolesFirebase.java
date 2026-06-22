package cl.catastrofescl.notifications.security;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class MapeadorRolesFirebase {

    private static final Map<String, String> ALIAS_FIREBASE_A_INTERNO = Map.ofEntries(
            Map.entry("ADMIN", "ADMINISTRADOR"),
            Map.entry("AUTHORITY", "AUTORIDAD"),
            Map.entry("OPERATOR", "OPERADOR"),
            Map.entry("CITIZEN", "PARTICULAR"),
            Map.entry("VOLUNTEER", "VOLUNTARIO")
    );

    private static final Set<String> ROLES_INTERNOS = Set.of(
            "ADMINISTRADOR", "AUTORIDAD", "OPERADOR", "PARTICULAR", "VOLUNTARIO", "REGISTRADO"
    );

    private MapeadorRolesFirebase() {
    }

    public static Set<String> normalizar(Collection<String> rolesCrudos) {
        Set<String> resultado = new HashSet<>();
        if (rolesCrudos == null) {
            return resultado;
        }
        for (String crudo : rolesCrudos) {
            String interno = normalizarUnRol(crudo);
            if (interno != null) {
                resultado.add(interno);
            }
        }
        return resultado;
    }

    public static String normalizarUnRol(String rolCrudo) {
        if (!StringUtils.hasText(rolCrudo)) {
            return null;
        }
        String t = rolCrudo.trim();
        if (ROLES_INTERNOS.contains(t)) {
            return t;
        }
        String mayus = t.toUpperCase();
        if (ROLES_INTERNOS.contains(mayus)) {
            return mayus;
        }
        return ALIAS_FIREBASE_A_INTERNO.getOrDefault(mayus, null);
    }
}
