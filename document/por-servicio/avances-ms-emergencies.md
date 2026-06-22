# Avances — CatástrofesCL
> Bitácora de progreso del proyecto. Actualizar al final de cada sesión o sprint de trabajo.

---

## Estado General del Proyecto

| Fase | Nombre | Estado | % Completado |
|---|---|---|---|
| Fase 0 | Fundamentos e Infraestructura | ⬜ No iniciado | 0% |
| Fase 1 | MS Identidad y Acceso | ⬜ No iniciado | 0% |
| Fase 2 | MS Operaciones de Recursos | ⬜ No iniciado | 0% |
| Fase 3 | MS Participación Ciudadana | ⬜ No iniciado | 0% |
| Fase 4 | MS Logística | ⬜ No iniciado | 0% |
| Fase 5 | MS Coordinación de Emergencias | 🔄 En progreso | 80% |
| Fase 6 | MS Notificaciones + Lambda | ⬜ No iniciado | 0% |
| Fase 7 | Integración RabbitMQ Completa | ⬜ No iniciado | 0% |
| Fase 8 | Frontend Completo | ⬜ No iniciado | 0% |
| Fase 9 | QA, Hardening y Producción | ⬜ No iniciado | 0% |

**Leyenda:** ⬜ No iniciado | 🔄 En progreso | ✅ Completado | 🔴 Bloqueado

---

## Formato de Entrada de Avance

```
## [Fecha] Sprint / Sesión — Descripción breve

**Integrante(s):** Nombre(s)
**Fase trabajada:** Fase X — Nombre

### Completado
- Lista de tareas terminadas

### En progreso
- Tareas iniciadas pero no terminadas

### Bloqueadores
- Problemas que impiden avanzar (crear issue en errores.md si aplica)

### Próximos pasos
- Qué se hará en la siguiente sesión
```

---

## Registro de Avances

### [2026-05-15] Sesión — Centros asociados + consumo de cola emergency.created

**Integrante(s):** Agente IA  
**Fase trabajada:** Fase 5 — MS Coordinación de Emergencias

#### Completado
- Tabla `centros_acopio_emergencia` y endpoints `GET /emergencies/active/centers`, `GET /emergencies/{id}/centers`.
- Declaración de emergencia con lista opcional `centrosAcopio` / alias JSON `centers`.
- Consumidor RabbitMQ sobre `emergency.created` con registro idempotente y marca de tiempo en detalle de emergencia.
- Evento `EmergenciaCreadaEvento` extendido con `centrosAsociados`.
- Documentado en `arreglos-y-cambios.md` como [ARR-007].
- Actualizados `prueba ms-emergencies.md` y `estado_actual_ms-emergiecies.ms` con centros asociados, nuevos GET, consumidor de cola y flujo tras `commit`.

#### Próximos pasos
- Integrar con catálogo real `ms-resources` / sincronizar centros canónicos cuando exista API definitiva.

---

### [2026-05-14] Sesión — Documento de estado `ms-emergencies`

**Integrante(s):** Agente IA  
**Fase trabajada:** Fase 5 — MS Coordinación de Emergencias

#### Completado
- Se generó el archivo `estado_actual_ms-emergiecies.ms` en la raíz del repositorio con análisis del microservicio (propósito, stack, API, seguridad, eventos RabbitMQ, datos, pruebas, alineación con plan Fase 5 y especificación MS-2, brechas y riesgos).

#### Próximos pasos
- Completar en código la tarea pendiente del plan: asociación de centros, misiones y necesidades con emergencias activas (o documentar diseño cross-MS).

---

### [2026-05-08] Sesión — GeoJSON de mapa y eventos para notificaciones

**Integrante(s):** Alfonso + Agente IA  
**Fase trabajada:** Fase 5 — MS Coordinación de Emergencias

#### Completado
- Se agregó `GET /emergencies/active/geojson` como endpoint público para entregar un `FeatureCollection` GeoJSON mínimo de polígonos activos.
- Se incorporó una consulta PostGIS optimizada con `ST_AsGeoJSON(zona_impacto::geometry)` para evitar cargar información innecesaria del dominio cuando el frontend solo necesita pintar el mapa.
- Se enriquecieron los eventos `emergency.created` y `announcement.published` con `versionEvento`, `fuente` y campos de contenido útiles para notificaciones internas y email.
- Se actualizaron tests unitarios y de integración; `mvn test` pasó con 26 tests.
- Se documentó el nuevo endpoint y la inspección de payloads RabbitMQ en `prueba ms-emergencies.md`.

#### En progreso
- Integración futura con `ms-notifications` para consumir `emergency.created` y `announcement.published`.

#### Bloqueadores
- `mvn verify` no pudo ejecutar tests de integración porque Testcontainers no detectó un entorno Docker válido desde Maven, aunque `docker info` respondió correctamente en consola. Registrado como `ERR-006`.

#### Próximos pasos
- Implementar `ms-notifications` o Lambda consumidora con idempotencia Redis para convertir estos eventos en notificaciones internas y correos.

---

### [2026-05-08] Sesión — Corrección de Swagger UI en ms-emergencies

**Integrante(s):** Alfonso + Agente IA  
**Fase trabajada:** Fase 5 — MS Coordinación de Emergencias

#### Completado
- Se diagnosticó el error `NoSuchMethodError` en `ControllerAdviceBean` al cargar Swagger/OpenAPI.
- Se actualizó Springdoc de `2.6.0` a `2.7.0` para compatibilidad con Spring Boot `3.4.0` y Spring Framework `6.2.0`.
- Se validó `mvn test` con 25 tests exitosos.
- Se reconstruyó `catastrofescl-ms-emergencies`; `GET /v3/api-docs` respondió `200` con JSON OpenAPI y `GET /swagger-ui.html` respondió `200`.

#### En progreso
- Pruebas manuales de endpoints del flujo de emergencias y anuncios.

#### Bloqueadores
- Ninguno.

#### Próximos pasos
- Entrar a `http://localhost:8082/swagger-ui.html` y continuar con la checklist de `prueba ms-emergencies.md`.

---

### [2026-05-08] Sesión — Arranque Docker de ms-emergencies

**Integrante(s):** Alfonso + Agente IA  
**Fase trabajada:** Fase 5 — MS Coordinación de Emergencias

#### Completado
- Se diagnosticó el error `Unable to resolve name [org.hibernate.spatial.dialect.postgis.PostgisDialect]`.
- Se eliminó el dialecto PostGIS obsoleto en la configuración principal y de tests.
- Se agregó `spring-boot-starter-actuator` para habilitar `/actuator/health`.
- Se validó `mvn test` con 25 tests exitosos.
- Se reconstruyó y levantó `catastrofescl-ms-emergencies`; Tomcat inició en `8082`, JPA inicializó correctamente y `/actuator/health` respondió `UP`.

#### En progreso
- Pruebas manuales de endpoints del flujo de emergencias y anuncios.

#### Bloqueadores
- Ninguno.

#### Próximos pasos
- Ejecutar la checklist de `prueba ms-emergencies.md` y validar creación de emergencia/anuncio contra RabbitMQ y PostgreSQL.

---

### [2026-05-07] Sesión — Docker Compose para ms-emergencies

**Integrante(s):** Alfonso + Agente IA  
**Fase trabajada:** Fase 5 — MS Coordinación de Emergencias

#### Completado
- Se diagnosticó que el log con `cl.duoc.productos`, `mysql-connector-j` y `UnknownHostException: mysql` provenía de un contenedor ajeno (`app`, imagen `ms-productos-app`), no de `ms-emergencies`.
- Se agregó `ms-emergencies` a `docker-compose.yml` con build local, perfil `dev`, puerto `8082` y conexión a PostgreSQL local vía `host.docker.internal`.
- Se actualizó `.env.example` con `POSTGRES_DOCKER_HOST`.
- Se actualizó `prueba ms-emergencies.md` con instrucciones para levantar y diagnosticar el contenedor correcto.
- Se validó la configuración con `docker compose config --services`.

#### En progreso
- Prueba manual del arranque completo del contenedor `catastrofescl-ms-emergencies` contra PostgreSQL local.

#### Bloqueadores
- Ninguno documentado. Si PostgreSQL local no acepta conexiones desde Docker, revisar firewall/servicio PostgreSQL y conexión vía `host.docker.internal`.

#### Próximos pasos
- Detener contenedores ajenos si aparecen en Docker Desktop y levantar `catastrofescl-ms-emergencies` con `docker compose up -d --build ms-emergencies`.

---

### [2026-05-07] Sesión — Ajuste de pruebas PostgreSQL local

**Integrante(s):** Alfonso + Agente IA  
**Fase trabajada:** Fase 5 — MS Coordinación de Emergencias

#### Completado
- Se actualizó `prueba ms-emergencies.md` para usar pgAdmin 4 como método principal de creación y verificación de la base `catastrofescl_emergencies`.
- Se reemplazaron los comandos obligatorios de `psql` por pasos con **Query Tool** para crear el rol de aplicación, habilitar `postgis`/`pgcrypto`, asignar permisos y verificar la instalación.
- Se registró el problema como `ERR-001` y su solución como `ARR-001`.

#### En progreso
- Validación manual del levantamiento local completo de `ms-emergencies`.

#### Bloqueadores
- Ninguno.

#### Próximos pasos
- Ejecutar el flujo de pgAdmin 4 y luego levantar `ms-emergencies` en perfil `dev`.

---

### [2026-04-19] Inicio del Proyecto — Documentación Base

**Integrante(s):** Equipo completo  
**Fase trabajada:** Fase 0 — Fundamentos

#### Completado
- Documento de Arquitectura de Solución entregado
- Especificaciones técnicas definidas (`especificaciones-tecnicas.md`)
- Plan de implementación por fases creado (`plan-de-implementacion.md`)
- Decisiones técnicas documentadas en `arreglos-y-cambios.md`
- Decisiones clave tomadas:
  - Firebase Auth como proveedor de identidad (reemplaza JWT propio)
  - OSRM para matching de voluntarios (rutas viales reales)
  - Umbrales configurables solo por Administrador, por centro
  - TanStack Query con hidratación SSR para tiempo real
  - RabbitMQ: Topic Exchange + DLQ + Idempotencia

#### En progreso
- Configuración del entorno local (Docker Compose)
- Creación del proyecto Firebase

#### Bloqueadores
- Ninguno

#### Próximos pasos
- Completar Fase 0: Docker Compose local + Firebase project + repositorios GitHub
- Iniciar Fase 1: estructura base del MS Identidad y Acceso

---

## Estadísticas de Progreso

### Microservicios
| Microservicio | Backend | Tests | Documentación API |
|---|---|---|---|
| MS Identidad y Acceso | ⬜ | ⬜ | ⬜ |
| MS Operaciones de Recursos | ⬜ | ⬜ | ⬜ |
| MS Participación Ciudadana | ⬜ | ⬜ | ⬜ |
| MS Logística | ⬜ | ⬜ | ⬜ |
| MS Coordinación de Emergencias | 🔄 | ✅ | ✅ |
| MS Notificaciones | ⬜ | ⬜ | ⬜ |

### Frontend
| Módulo | Implementado | Tests |
|---|---|---|
| Portal Ciudadano (Mapa + Donaciones) | ⬜ | ⬜ |
| Dashboard Autoridades/Operadores | ⬜ | ⬜ |
| Componentes shadcn/ui compartidos | ⬜ | ⬜ |
| Integración WebSocket STOMP | ⬜ | ⬜ |

### Infraestructura
| Componente | Local (Docker) | Producción (AWS) |
|---|---|---|
| PostgreSQL + PostGIS | ⬜ | ⬜ |
| Redis | ⬜ | ⬜ |
| RabbitMQ | ⬜ | ⬜ |
| Firebase Auth | ⬜ | ⬜ |
| API Gateway | ⬜ | ⬜ |
| EKS Cluster | N/A | ⬜ |
| Lambda + SES | ⬜ | ⬜ |
| CI/CD Pipeline | ⬜ | ⬜ |
