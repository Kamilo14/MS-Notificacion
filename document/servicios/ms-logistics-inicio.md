# MS Logística — Guía operativa (Fase 4)

> **Estado:** backend implementado (2026-06-04) | Puerto: **8085** | Repo/carpeta: `MSLogistica/`  
> Plan: [plan-de-implementacion.md](../plan-de-implementacion.md) — Fase 4  
> Progreso: [progreso.md](../progreso.md) | Detalle sesión: [por-servicio/avances-ms-logistics.md](../por-servicio/avances-ms-logistics.md)

## Arranque local (JAR / IDE)

```powershell
cd MSLogistica
# Variables: copiar .env.example → .env en raíz del monorepo o exportar
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

- Health: `http://localhost:8085/actuator/health`
- Swagger: `http://localhost:8085/swagger-ui.html` (si springdoc activo)

## Base de datos

- Host: `127.0.0.1:5432` (o `host.docker.internal` desde contenedor)
- Base: **`catastrofecl`** (minúsculas)
- Flyway: `flyway_schema_history_ms_logistics`
- Variable: `MS_LOGISTICS_DB_NAME=catastrofecl`

### Tablas propias

- `transferencias`, `items_transferencia`
- `misiones`, `rutas_voluntario`, `voluntarios_mision`
- PostGIS: `rutas_voluntario.coordenadas_*` + índices GIST

### Dependencias de otras fases

- Tabla `inventario` (ms-resources, Fase 2): el adapter JDBC valida stock; si la tabla no existe, lectura puede fallar de forma controlada en dev.

## Gateway (`MS-Api-Gateway`)

Ruta definida en `application.yml`:

```yaml
- id: ms-logistics
  uri: http://localhost:8085   # compose: http://ms-logistics:8085
  predicates:
    - Path=/transferencias/**, /misiones/**, /rutas-voluntario/**
```

**Prueba vía gateway:**

```http
GET http://localhost:8080/transferencias/{uuid}
X-Firebase-Uid: dev-admin-uid
X-Dev-Roles: ADMINISTRADOR
```

## APIs y permisos

| Método | Ruta | Permiso RBAC |
|--------|------|----------------|
| POST | `/transferencias` | `TRANSFERENCIA_SOLICITAR` |
| PATCH | `/transferencias/{id}/estado` | `TRANSFERENCIA_APROBAR` |
| GET | `/transferencias/{id}` | autenticado |
| POST | `/misiones` | `MISION_CREAR` |
| GET | `/misiones/{id}` | autenticado |
| POST | `/rutas-voluntario` | `RUTA_OFRECER` |
| GET | `/rutas-voluntario/matching/{misionId}` | `MISION_CREAR` o `RUTA_OFRECER` |

Roles → permisos: `src/main/resources/rbac/permisos-por-rol-logistics.yml`

## Eventos RabbitMQ (publicados)

| Routing key | Cuándo |
|-------------|--------|
| `transfer.created` | POST transferencia |
| `transfer.status.changed` | PATCH estado |
| `mission.assigned` | *(pendiente)* — clase `MisionAsignadaEvento` lista, sin wiring |

Exchange: `catastrofescl.events` (topic)

## OSRM (matching)

- Servicio: `MatchingOsrmService`
- URL base: `OSRM_BASE_URL` (default `http://router.project-osrm.org`)
- Fallback: distancia euclidiana si OSRM no responde (solo degradación; producción debe usar OSRM)

## Tests

```powershell
cd MSLogistica
.\mvnw.cmd test
```

**38 tests** — JUnit 5, Mockito, AssertJ, `@WebMvcTest` en controladores.

## Checklist Fase 4

- [x] Proyecto `ms-logistics` (`cl.catastrofescl.logistics`)
- [x] Flyway V1–V5
- [x] `POST /transferencias`, `PATCH /transferencias/{id}/estado`, `GET /transferencias/{id}`
- [x] `POST /misiones`, `GET /misiones/{id}`
- [x] `POST /rutas-voluntario`, `GET /rutas-voluntario/matching/{misionId}`
- [x] Eventos `transfer.created`, `transfer.status.changed`
- [x] Tests unitarios + WebMvcTest (38)
- [x] Health `:8085/actuator/health`
- [x] Documentación LOG en `document/` y `por-servicio/`
- [ ] Publicar `mission.assigned`
- [ ] Servicio en `compose.yaml` raíz
- [ ] E2E gateway + Postgres
- [ ] Frontend hooks
