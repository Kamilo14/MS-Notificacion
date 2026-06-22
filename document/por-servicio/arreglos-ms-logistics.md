# Arreglos y cambios — MS Logística (`LOG-`)

> Detalle íntegro. Tabla resumen: [../arreglos-y-cambios.md](../arreglos-y-cambios.md).

---

### [LOG-001] Scaffold → paquete `cl.catastrofescl.logistics`

- **Fecha:** 2026-06-04
- **Tipo:** Refactor / alineación monorepo
- **Descripción:** Reemplazo del paquete inicial `CatastrofeCL.MSLogistica` por estructura estándar `cl.catastrofescl.logistics` con capas `controller`, `service`, `repository`, `entity`, `dto`, `event`, `config`, `exception`, `seguridad`.
- **Archivos:** `MSLogistica/src/main/java/cl/catastrofescl/logistics/**`
- **Tests:** `MsLogisticsApplicationTests`

---

### [LOG-002] `ContextoUsuario` — accessor de record

- **Fecha:** 2026-06-04
- **Tipo:** Bugfix compilación
- **Descripción:** Uso de `contexto.usuarioId()` en lugar de `getUsuarioId()` (record Java).
- **Archivos:** `TransferenciaService.java`, `MisionService.java`, `RutaVoluntarioService.java`
- **Tests:** suite de servicios OK

---

### [LOG-003] OSRM — `Locale.ROOT` en URLs de coordenadas

- **Fecha:** 2026-06-04
- **Tipo:** Bugfix (Windows / locale es-CL)
- **Descripción:** Formato de lat/lng con coma decimal rompía URLs OSRM en entorno Windows; se fuerza `String.format(Locale.ROOT, ...)`.
- **Archivos:** `MatchingOsrmService.java`
- **Tests:** `MatchingOsrmServiceTest` (MockRestServiceServer)

---

### [LOG-004] RBAC YAML + modo dev gateway

- **Fecha:** 2026-06-04
- **Tipo:** Feature seguridad
- **Descripción:** Permisos por rol en `rbac/permisos-por-rol-logistics.yml`; `@PreAuthorize` por permiso; `FiltroAutenticacionDev` con headers `X-Firebase-Uid`, `X-Dev-Roles` (mismo patrón que emergencias).
- **Archivos:** `SeguridadConfig.java`, `FiltroAutenticacionDev.java`, `ProveedorPermisos.java`, `MapeadorRolesFirebase.java`
- **Tests:** `ProveedorPermisosTest`, `MapeadorRolesFirebaseTest`

---

### [LOG-005] Suite de tests unitarios y WebMvcTest (38)

- **Fecha:** 2026-06-04
- **Tipo:** Tests
- **Descripción:** Cobertura de happy/error path en servicios y contratos JSON de controladores; OSRM mockeado; inventario y eventos con Mockito.
- **Archivos:** `MSLogistica/src/test/java/cl/catastrofescl/logistics/**`
- **Comando:** `cd MSLogistica && .\mvnw.cmd test`

---

### [LOG-006] Flyway historial dedicado en BD compartida

- **Fecha:** 2026-06-04
- **Tipo:** Configuración
- **Descripción:** `spring.flyway.table=flyway_schema_history_ms_logistics` para coexistir con identity/emergencies en `catastrofecl`.
- **Archivos:** `application.yml`
