# Progreso consolidado — CatastrofeCL

> Actualizado: **2026-06-21** (MS Notificaciones Fase 6 backend + Lambda stub).  
> Revisar y ajustar % tras cada sprint.

## Fases del plan

| Fase | Nombre | Estado | % | Notas |
|------|--------|--------|---|-------|
| 0 | Fundamentos e infraestructura | 🔄 | 80% | `compose.yaml` raíz: PG local, Redis, RabbitMQ |
| 0.5 | MS Api Gateway | ✅ | 100% | Rutas, CORS, Firebase opcional, circuit breaker 8s |
| 1 | MS Identidad y Acceso | ✅ | 100% | Register, sync Firebase, roles, Flyway V1–V9 |
| 2 | MS Operaciones de Recursos | ⬜ | 0% | — |
| 3 | MS Participación Ciudadana | ⬜ | 0% | — |
| 4 | **MS Logística** | 🔄 | ~85% | Backend + 38 tests OK; pendiente compose, `mission.assigned`, E2E gateway — [ms-logistics-inicio.md](./servicios/ms-logistics-inicio.md) |
| 5 | MS Coordinación de Emergencias | 🔄 | ~80% | GeoJSON, eventos RabbitMQ, centros asociados, Docker OK |
| 6 | MS Notificaciones + Lambda | 🔄 | ~90% | Compose + E2E gateway OK; pendiente frontend STOMP |
| 7 | Integración RabbitMQ completa | 🔄 | 30% | EMG publica/consume; doc en `infra/RabbitMQConfig.md` |
| 8 | Frontend completo | 🔄 | 65% | Auth, emergencias, logística UI, sidebar unificado |
| 9 | QA, hardening y producción | ⬜ | 0% | — |

**Leyenda:** ⬜ No iniciado | 🔄 En progreso | ✅ Completado | 🔴 Bloqueado

## Estado por componente (integración E2E)

| Componente | Frontend | Backend | Integración | Estado |
|------------|----------|---------|-------------|--------|
| Autenticación (Firebase + identity) | ✅ | ✅ | 🔄 | Register/sync; BD `catastrofecl` (minúsculas) |
| Api Gateway (:8080) | ✅ | ✅ | 🔄 | 504 corregido (timeout CB); emergencias vía rewrite |
| Dashboard emergencias | ✅ | ✅ | 🔄 | TanStack Query; dibujo zonas por clics (sin DrawingManager) |
| Dashboard logística (FE) | 🔄 | ✅ | 🔄 | UI + mocks; listados `GET` pendientes en `:8085` |
| Sidebar / shell dashboard | ✅ | — | ✅ | Menú unificado RBAC; color consistente |
| Centros de acopio (catálogo ms-resources) | 🔄 | 🔄 | ⬜ | Ruta FE; gestión principal en emergencias |
| Transferencias / misiones (logística) | 🔄 | ✅ | 🔄 | APIs mutación OK; front con mocks; compose pendiente |
| Gestión ciudadana (FE) | 🔄 | ⬜ | ⬜ | Menú Necesidades/Donaciones; páginas pendientes |
| Inventario / donaciones | 🔄 | ⬜ | ⬜ | Inventario placeholder FE; ms-citizen Fase 3 |
| Notificaciones | ⬜ | 🔄 | 🔄 | Compose + gateway `:8080/notificaciones/**` OK; FE pendiente |

## Microservicios — detalle

| MS | Repo / carpeta | Backend | Tests | API docs | Puerto |
|----|----------------|---------|-------|----------|--------|
| Gateway | `MS-Api-Gateway` | ✅ | ⬜ | ⬜ | 8080 |
| Identity | `MS-Identidad-Acceso-CatastrofeCL/...` | ✅ | 🔄 | 🔄 | 8081 |
| Emergencies | `MS-Emergencias/ms-emergencies` | 🔄 | ✅ | ✅ | 8082 |
| Logistics | `MSLogistica` | 🔄 | ✅ (38 unit) | 🔄 | 8085 |
| Notifications | `MSNotificacion/MS-Notificacion/MSNotificacion` | 🔄 | ✅ (7 unit) | 🔄 | 8086 |
| Resources | *(futuro)* | ⬜ | ⬜ | ⬜ | 8083 |
| Frontend | `frontend-info` | — | ⬜ | — | 3000 (dashboard logística + emergencias) |

## Infraestructura local

| Componente | Local (Docker / host) | Notas |
|------------|----------------------|-------|
| PostgreSQL 15 + PostGIS | ✅ Host `127.0.0.1:5432` | Usar BD **`catastrofecl`** (minúsculas), no `CATASTROFECL` duplicada |
| Redis | ✅ | Compose |
| RabbitMQ | ✅ | Compose |
| Firebase Auth | ✅ | Proyecto `catastrofecl-cc6bd` |
| Compose raíz | ✅ | `CatastrofeCL-MS/compose.yaml` |

## Próximo hito recomendado

1. Añadir **ms-logistics** a `compose.yaml` y validar flujo vía gateway `:8080`.
2. Publicar evento **`mission.assigned`** y consumo en notificaciones (Fase 6).
3. **Frontend:** conectar listados reales (sustituir mocks) y páginas Gestión Usuarios / Ciudadana.
4. Cerrar Fase 4 al completar E2E local (opcional Testcontainers).
