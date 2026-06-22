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

- Añadir `ms-notifications` a `compose.yaml` raíz.
- E2E vía gateway `:8080/notificaciones/**`.
- Integración frontend (hook `useNotifications` + STOMP).
- Despliegue Lambda + SES en AWS (cuando se requiera entorno cloud).
- Publicar/consumir `mission.assigned` end-to-end con ms-logistics.

#### Próximos pasos

1. `mvn spring-boot:run` con BD `catastrofecl`, Redis y RabbitMQ locales.
2. Probar `GET /notificaciones` con header `X-Dev-Usuario-Id`.
3. Publicar evento de prueba a `catastrofescl.events` y verificar consumo.
