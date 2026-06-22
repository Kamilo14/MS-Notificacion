# Arreglos y Cambios — CatástrofesCL
> Este archivo documenta todos los cambios, correcciones y decisiones técnicas aplicadas durante el desarrollo.

---

## Formato de Registro

```
### [ARR-XXX] Título del Arreglo o Cambio
- **Fecha:** YYYY-MM-DD
- **Autor:** Nombre
- **Tipo:** Bugfix | Refactor | Feature | Config | Decisión técnica
- **Error relacionado:** ERR-XXX (si aplica)
- **Descripción del cambio:** Qué se modificó y por qué.
- **Archivos afectados:** Lista de archivos o clases modificadas.
- **Tests actualizados:** Sí / No / N/A
```

---

## Cambios Aplicados

### [ARR-010] RBAC compatible con Firebase (alias de roles + credenciales)
- **Fecha:** 2026-05-15
- **Autor:** Agente IA + equipo
- **Tipo:** Feature | Config | Decisión técnica
- **Error relacionado:** N/A
- **Descripción del cambio:** Los tokens Firebase suelen llevar roles en forma corta (`ADMIN`, `AUTHORITY`, ...) mientras el dominio usa codigos largos (`ADMINISTRADOR`, ...). Se añadio `MapeadorRolesFirebase` y se aplica en `FiltroAutenticacionFirebase`/`FiltroAutenticacionDev`. La matriz rol→permisos del modulo EMERGENCIAS pasa de codigo Java a `classpath:rbac/permisos-por-rol-emergencias.yml` cargada por `ProveedorPermisos`. Firebase Admin SDK: si `FIREBASE_CREDENTIALS_PATH` esta vacio, se usan Application Default Credentials (`GOOGLE_APPLICATION_CREDENTIALS` o GCP). Variables `FIREBASE_*` y `MS_EMERGENCIES_AUTH_DEV_MODE` en `application.yml`/`application-dev.yml`, Docker Compose y `.env.example`. `.gitignore` ignora JSON de cuenta de servicio típicos.
- **Archivos afectados:** `MapeadorRolesFirebase.java`, `ProveedorPermisos.java`, `FiltroAutenticacionFirebase.java`, `FiltroAutenticacionDev.java`, `FirebaseConfig.java`, `UsuarioAutenticado.java` (JavaDoc), `permisos-por-rol-emergencias.yml`, `application*.yml`, `docker-compose.yml`, `.env.example`, `.gitignore`, tests seguridad.
- **Tests actualizados:** Sí — `mvn test` OK.

### [ARR-009] No serializar getRoutingKey en eventos Rabbit (consumidor emergency.created)
- **Fecha:** 2026-05-15
- **Autor:** Agente IA + equipo
- **Tipo:** Bugfix
- **Error relacionado:** N/A
- **Descripción del cambio:** Jackson incluía `routingKey` en el JSON del cuerpo por el getter `getRoutingKey()` de `EventoDominio`. El mensaje ya lleva la routing key en propiedades AMQP; al deserializar en el `@RabbitListener` fallaba con `UnrecognizedPropertyException`. Se añadió `@JsonIgnore` en `getRoutingKey()` y `@JsonIgnoreProperties(ignoreUnknown = true)` en `EmergenciaCreadaEvento`, `AnuncioPublicadoEvento` y `EstadoEmergenciaCambiadoEvento`.
- **Archivos afectados:** `EmergenciaCreadaEvento.java`, `AnuncioPublicadoEvento.java`, `EstadoEmergenciaCambiadoEvento.java`
- **Tests actualizados:** Sí — `mvn test` ms-emergencies OK.

### [ARR-008] Paginacion de GET /announcements sin mezclar Sort del cliente con JPQL
- **Fecha:** 2026-05-15
- **Autor:** Agente IA + equipo
- **Tipo:** Bugfix
- **Error relacionado:** N/A
- **Descripción del cambio:** Con `@Query` y `ORDER BY` fijo, Spring Data concatenaba cualquier `sort` del `Pageable` (p. ej. `sort=string` desde Swagger) y Hibernate fallaba (`UnknownPathException` sobre `a.string`). `ServicioAnuncios.listarVigentes` ahora pasa solo `page`/`size` vía `PageRequest.of(...)`, preservando el orden definido en el repositorio. Se aclara la descripción en OpenAPI del controlador.
- **Archivos afectados:** `ServicioAnuncios.java`, `ControladorAnuncios.java`
- **Tests actualizados:** Sí — `ServicioAnunciosTest` OK tras el cambio.

### [ARR-007] Centros de acopio asociados a emergencias + consumidor RabbitMQ de demostracion
- **Fecha:** 2026-05-15
- **Autor:** Agente IA + equipo
- **Tipo:** Feature | Integración | Decisión técnica
- **Error relacionado:** N/A
- **Descripción del cambio:** Se agregó el modelo `centros_acopio_emergencia` (tabla propia de ms-emergencies, distinta del catálogo `centros` de ms-resources) para declarar puntos de acopio junto a una emergencia. La solicitud `POST /emergencies` acepta `centrosAcopio` o el alias JSON `centers`. Nuevos endpoints públicos `GET /emergencies/active/centers` y autenticado `GET /emergencies/{id}/centers`. El evento `emergency.created` ahora incluye `centrosAsociados`. Se configuró la cola `ms-emergencies.emergencia-creada` enlazada al topic existente con la misma routing key `emergency.created` (sin nuevas routing keys). Un `@RabbitListener` registra idempotencia en `registro_procesamiento_emergencia_creada` y opcionalmente en Redis; el detalle de emergencia expone `procesamientoColaEmergenciaCreadaEn`. La publicación del evento ocurre en `afterCommit` para coherencia con el consumidor.
- **Archivos afectados:** `V3__centros_acopio_emergencia_registro_cola.sql`, entidades y repositorios nuevos, `DeclararEmergenciaRequest`, `EmergenciaResponse`, `EmergenciaCreadaEvento`, `ServicioEmergencias`, `MapeadorEmergencias`, `ControladorEmergencias`, `SeguridadConfig`, `application.yml`, `application-test.yml`, `ConsumidorEventoEmergenciaCreada`, `ServicioRegistroProcesamientoEmergenciaCreada`, `pom.xml` (awaitility test), tests.
- **Tests actualizados:** Sí — `mvn test` unitario OK; IT requieren Docker (Testcontainers).

### [ARR-006] Agregar GeoJSON mínimo y eventos enriquecidos en ms-emergencies
- **Fecha:** 2026-05-08
- **Autor:** Alfonso + Agente IA
- **Tipo:** Feature | Integración
- **Error relacionado:** N/A
- **Descripción del cambio:** Se agregó un endpoint público `GET /emergencies/active/geojson` que devuelve un `FeatureCollection` mínimo para pintar polígonos de emergencias activas en el frontend. Además, los eventos `emergency.created` y `announcement.published` ahora incluyen metadatos (`versionEvento`, `fuente`) y contenido suficiente para consumidores de notificaciones internas y email.
- **Archivos afectados:** `ControladorEmergencias.java`, `ServicioEmergencias.java`, `RepositorioEmergencias.java`, DTOs GeoJSON de respuesta, eventos de dominio, `ServicioAnuncios.java`, tests, `prueba ms-emergencies.md`, `plan-de-implementacion.md`, `avances.md`
- **Tests actualizados:** Sí — `mvn test` pasó con 26 tests. `mvn verify` quedó bloqueado por `ERR-006` antes de ejecutar Testcontainers.

### [ARR-005] Actualizar Springdoc para compatibilidad con Spring Boot 3.4
- **Fecha:** 2026-05-08
- **Autor:** Alfonso + Agente IA
- **Tipo:** Bugfix | Config
- **Error relacionado:** ERR-005
- **Descripción del cambio:** Se actualizó `springdoc-openapi-starter-webmvc-ui` de `2.6.0` a `2.7.0` porque Spring Boot `3.4.0` usa Spring Framework `6.2.0`, donde cambió la API interna de `ControllerAdviceBean` usada por versiones anteriores de Springdoc al generar OpenAPI.
- **Archivos afectados:** `ms-emergencies/pom.xml`, `prueba ms-emergencies.md`, `errores.md`, `arreglos-y-cambios.md`, `avances.md`
- **Tests actualizados:** Sí — `mvn test` pasó con 25 tests; se reconstruyó Docker y `GET /v3/api-docs` + `GET /swagger-ui.html` respondieron `200`.

### [ARR-004] Agregar Actuator para health check de ms-emergencies
- **Fecha:** 2026-05-08
- **Autor:** Alfonso + Agente IA
- **Tipo:** Bugfix | Config
- **Error relacionado:** ERR-004
- **Descripción del cambio:** Se agregó `spring-boot-starter-actuator` porque el servicio ya configuraba `management.endpoints` y permitía `/actuator/health`, pero el endpoint no se registraba sin la dependencia.
- **Archivos afectados:** `ms-emergencies/pom.xml`, `errores.md`, `arreglos-y-cambios.md`, `avances.md`
- **Tests actualizados:** Sí — `mvn test` pasó con 25 tests; además se reconstruyó Docker y `GET /actuator/health` respondió `{"status":"UP"}`.

### [ARR-003] Quitar dialecto PostGIS obsoleto en Hibernate 6
- **Fecha:** 2026-05-08
- **Autor:** Alfonso + Agente IA
- **Tipo:** Bugfix | Config
- **Error relacionado:** ERR-003
- **Descripción del cambio:** Se eliminó la configuración explícita `org.hibernate.spatial.dialect.postgis.PostgisDialect` porque Hibernate 6.6 no la resuelve como dialecto. El servicio ahora deja que Hibernate detecte PostgreSQL y que `hibernate-spatial` registre los tipos y funciones PostGIS mediante `PostgisDialectContributor`.
- **Archivos afectados:** `ms-emergencies/src/main/resources/application.yml`, `ms-emergencies/src/test/resources/application-test.yml`, `prueba ms-emergencies.md`, `errores.md`, `arreglos-y-cambios.md`, `avances.md`
- **Tests actualizados:** Sí — `mvn test` pasó con 25 tests; además se reconstruyó y levantó `catastrofescl-ms-emergencies` con Docker Compose.

### [ARR-002] Agregar ms-emergencies a Docker Compose con PostgreSQL local
- **Fecha:** 2026-05-07
- **Autor:** Alfonso + Agente IA
- **Tipo:** Bugfix | Config | Documentación
- **Error relacionado:** ERR-002
- **Descripción del cambio:** Se agregó el servicio `ms-emergencies` a `docker-compose.yml`, construido desde `./ms-emergencies`, ejecutado en perfil `dev` y conectado a PostgreSQL local mediante `host.docker.internal`. Se documentó cómo identificar contenedores ajenos como `ms-productos-app` y cómo levantar/loguear el contenedor correcto `catastrofescl-ms-emergencies`.
- **Archivos afectados:** `docker-compose.yml`, `.env.example`, `prueba ms-emergencies.md`, `errores.md`, `arreglos-y-cambios.md`, `avances.md`
- **Tests actualizados:** N/A — se validó configuración con `docker compose config --services`

### [ARR-001] Documentar configuración de PostgreSQL local con pgAdmin 4
- **Fecha:** 2026-05-07
- **Autor:** Alfonso + Agente IA
- **Tipo:** Bugfix | Documentación
- **Error relacionado:** ERR-001
- **Descripción del cambio:** Se reemplazaron en la guía de pruebas los comandos obligatorios de `psql` por un flujo principal usando pgAdmin 4 y Query Tool. La creación de rol, base, extensiones PostGIS/pgcrypto, permisos y verificaciones ahora pueden ejecutarse sin depender del PATH de Windows.
- **Archivos afectados:** `prueba ms-emergencies.md`, `errores.md`, `arreglos-y-cambios.md`, `avances.md`
- **Tests actualizados:** N/A

### [DEC-007] Permisos granulares en BD + eliminación de combinaciones prohibidas
- **Fecha:** 2026-04-19
- **Autor:** Equipo
- **Tipo:** Decisión técnica — extensión del modelo de roles
- **Error relacionado:** N/A
- **Descripción del cambio:** Se eliminó la tabla `combinaciones_roles_prohibidas` (cualquier combinación de roles es válida). Se agregaron dos tablas nuevas: `permisos` (catálogo de permisos granulares por módulo, gestionables desde UI) y `roles_permisos` (asignación N:M de permisos a roles). Los permisos se cargan desde BD al autenticar, se cachean en Redis (TTL 5min) y se evalúan con `@PreAuthorize("hasAuthority('CODIGO_PERMISO')")` en lugar de `hasRole()`. Los custom claims de Firebase siguen almacenando solo los roles (array); los permisos se resuelven en runtime desde BD.
- **Archivos afectados:**
  - `especificaciones-tecnicas.md` — sección 12, dominio Identidad
  - `CLAUDE.md` — tabla de entidades + cheatsheet
  - Migraciones Flyway: `V3__create_permisos_tables.sql` + `V4__seed_permisos.sql`
- **Impacto en código:**
  - Nuevo `ServicioPermisos.java` — carga y cachea permisos por usuario desde BD
  - `FirebaseTokenFilter.java` — después de extraer roles, carga permisos desde `ServicioPermisos`
  - Todos los `@PreAuthorize` usan `hasAuthority('CODIGO')` en lugar de `hasRole('ROL')`
  - Eliminar `ServicioUsuarioRol.validarCombinacion()` — ya no es necesario
- **Tests actualizados:** Pendiente — actualizar en Fase 1
- **Fecha:** 2026-04-19
- **Autor:** Equipo
- **Tipo:** Decisión técnica — cambio de modelo de datos
- **Error relacionado:** N/A
- **Descripción del cambio:** Se eliminó el campo `rol varchar` de la tabla `usuarios` y se creó una estructura N:M compuesta por tres tablas nuevas: `roles` (catálogo de los 5 roles del sistema), `usuarios_roles` (asignación múltiple de roles a un usuario) y `combinaciones_roles_prohibidas` (restricciones de incompatibilidad entre roles). La motivación es permitir que un usuario tenga múltiples roles simultáneos (ej: AUTORIDAD + VOLUNTARIO). Los permisos siguen gestionándose en Spring Security, no en BD. Firebase custom claims pasa ahora un array `"roles": ["AUTORIDAD","VOLUNTARIO"]` en lugar de un string simple.
- **Archivos afectados:**
  - `especificaciones-tecnicas.md` — sección 12, dominio Identidad
  - `CLAUDE.md` — tabla de referencia de entidades
  - Migración Flyway: reemplazar `V1__create_users_table.sql` por versión sin columna `rol` + nueva `V2__create_roles_tables.sql`
- **Impacto en código:**
  - `Usuario.java` → eliminar campo `rol`, agregar relación `@ManyToMany` con `Rol`
  - `FirebaseTokenFilter.java` → leer array `roles` del custom claim en lugar de string
  - `ServicioUsuarioRol.java` → nuevo servicio con validación de `combinaciones_roles_prohibidas`
  - `@PreAuthorize` existentes → cambiar `hasRole('X')` por `hasAnyRole('X')` donde aplique
- **Tests actualizados:** Pendiente — actualizar en Fase 1

---

## Decisiones Técnicas Documentadas

### [DEC-001] Firebase Auth como proveedor de identidad
- **Fecha:** 2026-04-19
- **Decisión:** Usar Firebase Authentication en lugar de implementar JWT propio con Spring Security.
- **Razón:** Firebase Auth provee gestión robusta de tokens, refresh automático, verificación de email, y soporta múltiples providers (email/password, Google) out-of-the-box. Los custom claims de Firebase permiten almacenar el rol del usuario directamente en el token.
- **Impacto:** MS Identidad valida tokens Firebase con Firebase Admin SDK en lugar de generar JWT propios.

### [DEC-002] Base de datos compartida en fase inicial
- **Fecha:** 2026-04-19
- **Decisión:** Una sola instancia PostgreSQL+PostGIS en RDS para todos los microservicios.
- **Razón:** Simplifica las relaciones entre dominios y mantiene consistencia transaccional sin incrementar la complejidad operativa en esta etapa del proyecto.
- **Impacto:** Cada microservicio accede solo a sus tablas de dominio. En futuras iteraciones se puede evolucionar a BD independiente por microservicio.

### [DEC-003] OSRM para matching de voluntarios
- **Fecha:** 2026-04-19
- **Decisión:** Usar OSRM en lugar de distancia euclidiana para el matching de voluntarios de transporte.
- **Razón:** La distancia "en línea recta" es inadecuada para Chile, donde la geografía montañosa, ríos y accidentes geográficos hacen que la distancia real vial sea muy diferente a la euclidiana. OSRM usa datos reales de OpenStreetMap.
- **Impacto:** MS Logística hace llamadas HTTP al OSRM API. En producción se recomienda self-hosting en EC2.

### [DEC-004] Umbrales de criticidad configurables solo por Administrador
- **Fecha:** 2026-04-19
- **Decisión:** Solo el rol Administrador puede modificar los umbrales de stock por centro de acopio.
- **Razón:** Los umbrales determinan cuándo se generan alertas críticas y sugerencias de redistribución. Una configuración incorrecta podría generar falsas alarmas o ignorar escasez real. El rol Autoridad puede ver pero no modificar.
- **Impacto:** Endpoint `PATCH /centers/:id/thresholds` protegido con `@PreAuthorize("hasRole('ADMIN')")`.

### [DEC-005] TanStack Query con hidratación SSR para datos en tiempo real
- **Fecha:** 2026-04-19
- **Decisión:** Usar hidratación de servidor (SSR → Client) con TanStack Query v5 en lugar de recargar la página para actualizar datos en tiempo real.
- **Razón:** Durante emergencias, el panel de autoridades y el mapa público deben actualizarse sin interrumpir la experiencia del usuario. La hidratación permite tener datos frescos del servidor en el primer render y luego actualizar silenciosamente en background.
- **Impacto:** Uso de `HydrationBoundary` en server components + `useQuery` con `refetchInterval` en client components.

---

## Plantilla Rápida

```markdown
### [ARR-001] 
- **Fecha:** 
- **Autor:** 
- **Tipo:** 
- **Error relacionado:** 
- **Descripción del cambio:** 
- **Archivos afectados:** 
- **Tests actualizados:** 
```
