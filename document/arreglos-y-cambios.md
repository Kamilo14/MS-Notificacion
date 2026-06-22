# Arreglos y cambios consolidados — CatástrofesCL

> Prefijos evitan colisión de `ARR-001` entre repos.  
> Detalle íntegro por carpeta: [por-servicio/](./por-servicio/).

## Formato

```
### [EMG-010] Título
- Fecha, Autor, Tipo, Descripción, Archivos, Tests
```

---

## MS Emergencias (`EMG-`)

| ID | Título | Fecha |
|----|--------|-------|
| EMG-010 | RBAC Firebase (MapeadorRolesFirebase + YAML permisos) | 2026-05-15 |
| EMG-009 | `@JsonIgnore` routingKey en eventos Rabbit | 2026-05-15 |
| EMG-008 | Paginación GET /announcements sin Sort inválido | 2026-05-15 |
| EMG-007 | Centros acopio por emergencia + consumidor `emergency.created` | 2026-05-15 |
| EMG-006 | GeoJSON activas + eventos enriquecidos | 2026-05-08 |
| EMG-005 | Springdoc 2.7 (Boot 3.4) | 2026-05-08 |
| EMG-004 | Actuator health | 2026-05-08 |
| EMG-003 | Quitar dialecto PostGIS obsoleto | 2026-05-08 |
| EMG-002 | ms-emergencies en Docker Compose | 2026-05-07 |
| EMG-001 | Guía PostgreSQL pgAdmin | 2026-05-07 |

Detalle: [por-servicio/arreglos-ms-emergencies.md](./por-servicio/arreglos-ms-emergencies.md)

---

## MS Api Gateway (`GW-`)

| ID | Título | Fecha |
|----|--------|-------|
| GW-012 | Implementación API Gateway (Fase 0.5) | 2026-05-05 |
| GW-013 | CORS centralizado `CorsConfig` | 2026-05-06 |
| GW-014 | Timeout circuit breaker / Resilience4j 8s (compose local) | 2026-05-15 |

Detalle: [por-servicio/arreglos-ms-gateway.md](./por-servicio/arreglos-ms-gateway.md) (si existe) o `MS-Api-Gateway/document/`

---

## MS Identidad (`IDN-`)

| ID | Título | Fecha |
|----|--------|-------|
| IDN-001 | Sync automático Firebase → BD | 2026-04-24 |
| IDN-002 | Rol REGISTRADO sin privilegios | 2026-05-04 |
| IDN-003 | Firebase enabled local | 2026-05-04 |
| IDN-004 | Cache `simple` sin Redis en dev | 2026-05-04 |
| IDN-007 | `POST /auth/register` unificado | 2026-05-04 |
| IDN-008 | Autorización híbrida rol + permiso | 2026-05-04 |
| IDN-009 | Simplificación a solo rol (etapa) | 2026-05-04 |
| IDN-010 | Cierre Fase 1 + tests | 2026-05-04 |
| IDN-014 | Registro: recuperar UID si EMAIL_EXISTS en Firebase | 2026-05-15 |
| IDN-015 | JDBC unificado `catastrofecl` (minúsculas) | 2026-05-15 |

Detalle: [por-servicio/arreglos-ms-identity.md](./por-servicio/arreglos-ms-identity.md)

---

## Frontend (`FE-`)

| ID | Título | Fecha |
|----|--------|-------|
| FE-001 | Sync Firebase manual | 2026-04-24 |
| FE-013 | Validación RUT + flujo registro | 2026-05-13 |
| FE-014 | Doc consumo ms-emergencies | 2026-05-09 |
| FE-015 | `POST /auth/register` sin Bearer en interceptor | 2026-05-15 |
| FE-016 | Shell dashboard único + módulo logística FE | 2026-06-04 |
| FE-017 | Color sidebar unificado (`sidebar--app`) | 2026-06-04 |
| FE-018 | Menú lateral definitivo por módulos | 2026-06-04 |

Detalle: [por-servicio/arreglos-frontend-info.md](./por-servicio/arreglos-frontend-info.md)

---

## MS Logística (`LOG-`)

| ID | Título | Fecha |
|----|--------|-------|
| LOG-001 | Paquete `cl.catastrofescl.logistics` (reemplazo scaffold) | 2026-06-04 |
| LOG-002 | `ContextoUsuario` — `usuarioId()` en record | 2026-06-04 |
| LOG-003 | OSRM — `Locale.ROOT` en coordenadas (Windows) | 2026-06-04 |
| LOG-004 | RBAC YAML + `FiltroAutenticacionDev` | 2026-06-04 |
| LOG-005 | Suite tests 38 (JUnit + Mockito + WebMvcTest) | 2026-06-04 |
| LOG-006 | Flyway `flyway_schema_history_ms_logistics` | 2026-06-04 |

Detalle: [por-servicio/arreglos-ms-logistics.md](./por-servicio/arreglos-ms-logistics.md)

---

## Decisiones globales (`DEC-`)

| ID | Título |
|----|--------|
| DEC-001 | Firebase Auth como IdP |
| DEC-002 | BD PostgreSQL compartida (tablas por dominio) |
| DEC-003 | OSRM para matching voluntarios |
| DEC-004 | Umbrales criticidad solo Administrador |
| DEC-005 | TanStack Query + SSR hidratación |
| DEC-007 | Permisos granulares en BD (N:M roles_permisos) |
| DEC-010 | API Gateway Spring Cloud (no AWS API GW) |
| DEC-011 | PostgreSQL/Redis en Docker local |
| DEC-012 | Un repo por microservicio |
| DEC-013 | Nomenclatura por capa (español en dominio) |

Detalle completo: [por-servicio/arreglos-frontend-info.md](./por-servicio/arreglos-frontend-info.md) (sección DEC).
