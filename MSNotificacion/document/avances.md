# Avances consolidados — CatástrofesCL

> Bitácora unificada del workspace `CatastrofeCL-MS`.  
> Histórico completo por carpeta: [por-servicio/](./por-servicio/).

## Formato de entrada

```markdown
### [YYYY-MM-DD] Título
**Origen:** FE | GW | IDN | EMG | LOG | INFRA
**Fase:** N — Nombre
**Integrante(s):** …

#### Completado
- …
```

---

## Registro cronológico (únicos / consolidados)

### [2026-06-21] MS Notificaciones — Fase 6 backend + Lambda email stub
**Origen:** NOT  
**Fase:** 6 — MS Notificaciones + Lambda Email  
**Detalle:** [por-servicio/avances-ms-notifications.md](./por-servicio/avances-ms-notifications.md)

#### Completado
- `ms-notifications` puerto **8086**: Flyway V1–V3, APIs REST, WebSocket STOMP, consumidor `notifications.queue`, idempotencia Redis+BD, Lambda Node.js stub.
- **7 tests** unitarios OK.

#### Pendiente
- Compose raíz, E2E gateway, integración frontend.

---

### [2026-06-04] Frontend — módulo logística dashboard + sidebar unificado
**Origen:** FE  
**Fase:** 8 — Frontend Completo (integración Fase 4)  
**Detalle:** [por-servicio/avances-frontend-info.md](./por-servicio/avances-frontend-info.md) | Arreglos: `FE-016`, `FE-017`, `FE-018`

#### Completado
- Rutas `/dashboard/logistica/*` (resumen, transferencias, misiones, rutas voluntario, matching OSRM, inventario, centros).
- Servicios/hooks logística + permisos RBAC; mocks para listados sin `GET` en backend.
- Shell único: un `Sidebar` en `dashboard/layout.tsx`, `DashboardThemeProvider`, sin sidebars duplicados.
- Menú lateral definitivo (Gestión Usuarios, Emergencias, Centros, Logística, Inventario, Gestión Ciudadana).
- Color sidebar unificado (`sidebar--app`, `#10170D`); fix loader Google Maps y dibujo zonas sin DrawingManager.

#### Pendiente
- Páginas `/dashboard/usuarios`, `/dashboard/ciudadana/necesidades`, `/dashboard/ciudadana/donaciones`.
- E2E logística vía gateway; sustituir mocks por APIs listado.

---

### [2026-06-04] MS Logística — Fase 4 backend + suite de tests (38)
**Origen:** LOG  
**Fase:** 4 — MS Logística  
**Detalle:** [por-servicio/avances-ms-logistics.md](./por-servicio/avances-ms-logistics.md) | Arreglos: [por-servicio/arreglos-ms-logistics.md](./por-servicio/arreglos-ms-logistics.md)

#### Completado
- Carpeta **`MSLogistica/`**, artefacto `ms-logistics`, paquete `cl.catastrofescl.logistics`, puerto **8085**
- Flyway V1–V5 + historial `flyway_schema_history_ms_logistics` en BD **`catastrofecl`**
- APIs: transferencias (flujo estados + inventario JDBC en `RECIBIDA`), misiones, rutas voluntario, matching OSRM
- Seguridad: RBAC `permisos-por-rol-logistics.yml`, modo dev (headers gateway), RFC 7807
- Eventos RabbitMQ: `transfer.created`, `transfer.status.changed`
- **38 tests** (`mvn test` OK): `TransferenciaServiceTest`×8, controladores WebMvcTest, `MatchingOsrmServiceTest`, RBAC
- Documentación LOG actualizada (`CLAUDE.md` v1.8, `progreso.md`, `plan` Fase 4, `ms-logistics-inicio.md`)

#### Pendiente
- Publicar `mission.assigned`; servicio en `compose.yaml`; E2E gateway; frontend hooks

#### Próximos pasos
- `docker compose` + validación `localhost:8080/transferencias/**`
- Integración `frontend-info` (TanStack Query)

---

### [2026-05-15] Centros asociados + consumo cola `emergency.created`
**Origen:** EMG  
**Fase:** 5 — MS Emergencias  
Ver detalle: [por-servicio/avances-ms-emergencies.md](./por-servicio/avances-ms-emergencies.md)

---

### [2026-05-15] RBAC Firebase + compose stack local (identity/emergencies/gateway)
**Origen:** INFRA / EMG / IDN  
**Fase:** 0–5  
- BD unificada `catastrofecl` (minúsculas); duplicado `CATASTROFECL` identificado en pgAdmin.
- Registro front vía `POST /auth/register`; recuperación `EMAIL_EXISTS` en identity.
- Gateway: circuit breaker timeout 8s (evita 504 en emergencias).

---

### [2026-05-14] Documento estado `ms-emergencies`
**Origen:** EMG  
**Fase:** 5  
Archivo: `MS-Emergencias/ms-emergencies/estado_actual_ms-emergiecies.ms`

---

### [2026-05-09] Documentación consumo ms-emergencies para frontend
**Origen:** FE  
**Fase:** 8 — Frontend  
- `ENDPOINTS.md`, mapa GeoJSON, anuncios vía gateway `:8080`.

---

### [2026-05-09] Refactor panel emergencias + Sidebar
**Origen:** FE  
**Fase:** 8  
- Panel lateral, tabs, iconos Lucide, Tailwind v3 estable.

---

### [2026-05-08] GeoJSON + eventos enriquecidos + Swagger + Docker EMG
**Origen:** EMG  
**Fase:** 5  
- `GET /emergencies/active/geojson`, Springdoc 2.7, Actuator, PostGIS Hibernate 6.

---

### [2026-05-07] Docker Compose ms-emergencies + guía pgAdmin
**Origen:** EMG  
**Fase:** 5  

---

### [2026-05-06] CORS centralizado en Gateway
**Origen:** GW  
**Fase:** 0.5  

---

### [2026-05-05] MS Gateway implementado (Fase 0.5)
**Origen:** GW  
**Fase:** 0.5  
Firebase filter, rate limit, rutas a microservicios, RFC 7807.

---

### [2026-05-05] Servicios API front + registro UI
**Origen:** FE  
**Fase:** 8  
- `auth.service.ts`, `apiClient.ts`, `RegisterPage`, redirect login.

---

### [2026-05-05] Docker ms-identity
**Origen:** IDN  
**Fase:** 1  

---

### [2026-05-04] Cierre Fase 1 — MS Identidad (consolidado)
**Origen:** IDN / GW / FE  
**Fase:** 1  
Entrada única; detalle duplicado eliminado en copias locales.

#### Completado
- Flyway V1–V8 (+ V9 `pais` varchar en despliegues recientes).
- Endpoints auth: register, sync, sync/system, invitaciones, usuarios, roles.
- Rol por defecto `REGISTRADO`; custom claims desde BD.
- Tests unitarios + Testcontainers (pendiente ejecución local equipo).
- Cloud Function onCreate documentada.

#### Próximos pasos
- Asignación roles en pgAdmin + `POST /auth/firebase/sync` + nuevo idToken.

---

### [2026-04-24] Sync automático Firebase → BD
**Origen:** IDN  
**Fase:** 1  
`POST /auth/firebase/sync/system` + `X-Sync-Secret`.

---

### [2026-04-19] Inicio — documentación base
**Origen:** Todos  
**Fase:** 0  
Arquitectura, especificaciones, plan por fases, RabbitMQ topic + DLQ.

---

## Por origen (índice rápido)

| Origen | Archivo histórico |
|--------|-------------------|
| **EMG** | [por-servicio/avances-ms-emergencies.md](./por-servicio/avances-ms-emergencies.md) |
| **GW** | [por-servicio/avances-ms-gateway.md](./por-servicio/avances-ms-gateway.md) |
| **IDN** | [por-servicio/avances-ms-identity.md](./por-servicio/avances-ms-identity.md) |
| **FE** | [por-servicio/avances-frontend-info.md](./por-servicio/avances-frontend-info.md) |

> **Nota:** El detalle FE vive en `por-servicio/avances-frontend-info.md`; este archivo consolidado es la referencia principal.
