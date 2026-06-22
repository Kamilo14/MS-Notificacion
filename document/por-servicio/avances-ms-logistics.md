# Avances — MS Logística (`ms-logistics`)

> Detalle de sesiones en `MSLogistica/`. Resumen consolidado: [../avances.md](../avances.md) (**Origen:** LOG).

## Estado del módulo (2026-06-04)

| Área | Estado | Notas |
|------|--------|-------|
| Proyecto Spring Boot 3.4 | ✅ | Carpeta `MSLogistica/`, artefacto `ms-logistics`, puerto **8085** |
| Paquete Java | ✅ | `cl.catastrofescl.logistics` |
| Flyway V1–V5 | ✅ | Tabla historial `flyway_schema_history_ms_logistics` |
| APIs REST | ✅ | Transferencias, misiones, rutas voluntario + matching |
| Seguridad dev + RBAC | ✅ | `SeguridadConfig`, `FiltroAutenticacionDev`, YAML permisos |
| Eventos RabbitMQ | 🔄 | `transfer.created`, `transfer.status.changed` publicados; `mission.assigned` definido, sin publicar aún |
| Tests unitarios | ✅ | **38** tests, `mvn test` OK |
| Testcontainers / E2E | ⬜ | Pendiente |
| Docker Compose raíz | ⬜ | Servicio `ms-logistics` no añadido |
| Frontend | ⬜ | Sin servicios/hooks |

---

## [2026-06-04] Fase 4 — Implementación backend + suite de tests

**Fase trabajada:** 4 — MS Logística  
**Carpeta código:** `MSLogistica/src/main/java/cl/catastrofescl/logistics/`

### Completado

**Infra y configuración**

- `pom.xml`: JPA, PostGIS (hibernate-spatial), Flyway, Security, RabbitMQ, Redis, springdoc, snakeyaml
- `application.yml`: puerto 8085, BD `catastrofecl` (`MS_LOGISTICS_DB_NAME`), Flyway dedicado
- `application-dev.yml`: RabbitMQ, Redis, modo auth dev (headers gateway)
- `.env.example`, `Dockerfile`
- `application-test.yml`: excluye DataSource/Flyway/Rabbit/Redis para tests livianos

**Migraciones Flyway**

- `V1__create_transfers_table.sql`
- `V2__create_transfer_items_table.sql`
- `V3__create_missions_table.sql`
- `V4__create_volunteer_routes_table.sql` (GIST)
- `V5__create_mission_volunteers_table.sql` (UNIQUE mision + ruta)

**Endpoints (español, vía gateway `:8080`)**

| Método | Ruta | Permiso |
|--------|------|---------|
| POST | `/transferencias` | `TRANSFERENCIA_SOLICITAR` |
| PATCH | `/transferencias/{id}/estado` | `TRANSFERENCIA_APROBAR` |
| GET | `/transferencias/{id}` | autenticado |
| POST | `/misiones` | `MISION_CREAR` |
| GET | `/misiones/{id}` | autenticado |
| POST | `/rutas-voluntario` | `RUTA_OFRECER` |
| GET | `/rutas-voluntario/matching/{misionId}` | `MISION_CREAR` o `RUTA_OFRECER` |

**Servicios**

- `TransferenciaService`: flujo estados + inventario JDBC en `RECIBIDA`
- `MisionService`, `RutaVoluntarioService`, `MatchingOsrmService` (OSRM + fallback euclidiano)
- `InventarioJdbcAdapter`: lectura/actualización tabla `inventario` (depende Fase 2)
- `PublicadorEventos`: topic `catastrofescl.events`
- Excepciones RFC 7807 (`ManejadorExcepcionesGlobal`)

**Tests (38)**

- Servicios: `TransferenciaServiceTest` (8), `MisionServiceTest`, `RutaVoluntarioServiceTest`, `MatchingOsrmServiceTest`, `InventarioJdbcAdapterTest`, `PublicadorEventosTest`, `GeometriaMapperTest`
- Controladores: `TransferenciaControllerTest`, `MisionControllerTest`, `RutaVoluntarioControllerTest`
- Seguridad: `ProveedorPermisosTest`, `MapeadorRolesFirebaseTest`
- Smoke: `MsLogisticsApplicationTests`

### En progreso / pendiente

- Publicar `mission.assigned` al asignar voluntario a misión
- Servicio en `compose.yaml` raíz + imagen Docker local
- Prueba manual E2E: gateway → ms-logistics con Postgres local
- Integración frontend (`frontend-info`): hooks TanStack Query
- Tests integración Testcontainers (opcional, patrón EMG)

### Próximos pasos

1. Añadir `ms-logistics` a `CatastrofeCL-MS/compose.yaml`
2. Validar `GET http://localhost:8080/transferencias/{id}` con headers dev
3. Implementar publicación `mission.assigned` y test asociado
4. Servicios TypeScript en `frontend-info`
