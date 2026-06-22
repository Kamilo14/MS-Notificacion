# Avances — MS Notificaciones (`NOT`)

> Bitácora operativa del microservicio `ms-notifications` (puerto **8086**).

---

### [2026-06-21] Fase 6 — Backend ms-notifications + Lambda email (stub local)

**Fase:** 6 — MS Notificaciones + Lambda Email  
**Integrante(s):** Cursor Agent

#### Completado

- Reestructura a paquete `cl.catastrofescl.notifications` (artefacto `ms-notifications`).
- Flyway V1–V3: `notificaciones`, `preferencias_notificacion`, `eventos_procesados`.
- Entidades JPA: `Notificacion`, `PreferenciaNotificacion`, `EventoProcesado`.
- APIs REST:
  - `GET /notificaciones` (paginado)
  - `PATCH /notificaciones/:id/leer`
  - `GET /notificaciones/preferencias`
  - `PATCH /notificaciones/preferencias`
- Seguridad modo dev (headers `X-Dev-*` / gateway), RFC 7807, Swagger.
- RabbitMQ: cola `notifications.queue` (binding `#`), `email.queue`, DLQ `notifications.dlq` / `email.dlq`.
- Consumidor con idempotencia Redis `processed:{eventId}` + tabla `eventos_procesados`.
- WebSocket STOMP: endpoint `/ws-notificaciones`, canal `/topic/notificaciones/{usuarioId}`.
- Lambda Node.js `lambda/email-sender/` con modo stub local (`EMAIL_LOCAL_STUB=true`).
- Script SQL manual: `scripts/sql/init-ms-notifications.sql`.
- **7 tests** unitarios (`mvn test` OK).

#### Pendiente

- Integración frontend (hook `useNotifications` + STOMP).
- Despliegue Lambda + SES en AWS (cuando se requiera entorno cloud).
- Publicar/consumir `mission.assigned` end-to-end con ms-logistics.

---

### [2026-06-22] Compose + E2E gateway (`:8080/notificaciones/**`)

**Fase:** 6 — MS Notificaciones + Lambda Email  
**Integrante(s):** Cursor Agent

#### Completado

- `Dockerfile` + `.dockerignore` en `MSNotificacion/MS-Notificacion/MSNotificacion/`.
- Servicio `ms-notifications` en `compose.yaml` raíz (puerto 8086, RabbitMQ, Redis, Flyway, auth dev).
- Gateway: `MS_NOTIFICATIONS_URI=http://ms-notifications:8086` + `depends_on` healthy.
- E2E: `GET http://localhost:8080/notificaciones` vía gateway (modo dev con `X-Firebase-Uid`).
- Fix Flyway V3: tabla renombrada a `notificaciones_eventos_procesados` (evita colisión con `ms-resources.eventos_procesados`).

#### Próximos pasos

1. Integración frontend (`useNotifications` + STOMP).
2. Publicar evento de prueba y verificar notificación in-app + WebSocket.
