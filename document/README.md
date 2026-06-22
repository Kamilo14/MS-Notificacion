# Documentación central — CatastrofeCL-MS

> **Capa global** del workspace. Cada microservicio y el frontend mantienen además su propia carpeta de documentación con `avances.md`, `errores.md` y `arreglos-y-cambios.md`.

## Empezar aquí (agente / desarrollador)

1. **[CLAUDE.md](./CLAUDE.md)** — instrucciones maestras, protocolo y tabla de rutas por módulo  
2. [plan-de-implementacion.md](./plan-de-implementacion.md) — fases y checklists  
3. [errores.md](./errores.md) — errores consolidados + índice a errores por MS  

## Índice — Capa central (`document/`)

| Archivo | Uso |
|---------|-----|
| **[CLAUDE.md](./CLAUDE.md)** | Instrucciones maestras para el agente |
| [progreso.md](./progreso.md) | Tablero rápido: fases, % y estado por componente |
| [plan-de-implementacion.md](./plan-de-implementacion.md) | Plan maestro por fases (checklists) |
| [avances.md](./avances.md) | Bitácora consolidada (`**Origen:** FE \| GW \| IDN \| EMG \| …`) |
| [arreglos-y-cambios.md](./arreglos-y-cambios.md) | ARR/DEC unificados con prefijos por microservicio |
| [errores.md](./errores.md) | Errores globales + índice a `errores.md` de cada módulo |
| [especificaciones-tecnicas.md](./especificaciones-tecnicas.md) | Modelo de datos, APIs, RabbitMQ, permisos |

## Documentación por módulo (Capa 2)

Cada fila es la carpeta donde viven **`avances.md`**, **`errores.md`** y **`arreglos-y-cambios.md`** del módulo.

| Código | Módulo | Carpeta documentación |
|--------|--------|------------------------|
| `GW` | MS Api Gateway | `MS-Api-Gateway/document/` |
| `IDN` | MS Identidad y Acceso | `MS-Identidad-Acceso-CatastrofeCL/MS-Identidad-y-Acceso-CatastrofeCL/document/` |
| `EMG` | MS Emergencias | `MS-Emergencias/ms-emergencies/` *(archivos en raíz del repo, sin `document/`)* |
| `FE` | Frontend info | `frontend-info/document/` |
| `LOG` | MS Logística | `MSLogistica/document/` |

Guías de arranque: [servicios/](./servicios/).

## Por servicio (puertos / contexto)

| Servicio | Puerto | Guía |
|----------|--------|------|
| MS Api Gateway | 8080 | [servicios/ms-gateway.md](./servicios/ms-gateway.md) |
| MS Identidad y Acceso | 8081 | [servicios/ms-identity.md](./servicios/ms-identity.md) |
| MS Emergencias | 8082 | [servicios/ms-emergencies.md](./servicios/ms-emergencies.md) |
| **MS Logística** | **8085** | [servicios/ms-logistics-inicio.md](./servicios/ms-logistics-inicio.md) |
| Frontend info | 3000 | [servicios/frontend-info.md](./servicios/frontend-info.md) |

## Infra local

- Stack Docker raíz: `../compose.yaml`
- RabbitMQ: [infra/RabbitMQConfig.md](./infra/RabbitMQConfig.md)

## Histórico (no editar como fuente viva)

- [por-servicio/](./por-servicio/) — exportaciones `avances-*.md`, `arreglos-*.md`
- LOG (detalle vivo): [por-servicio/avances-ms-logistics.md](./por-servicio/avances-ms-logistics.md), [por-servicio/arreglos-ms-logistics.md](./por-servicio/arreglos-ms-logistics.md)

## Nomenclatura de trazabilidad

| Prefijo | Servicio |
|---------|----------|
| `FE-` | frontend-info |
| `GW-` | MS-Api-Gateway |
| `IDN-` | MS Identidad y Acceso |
| `EMG-` | MS Emergencias |
| `LOG-` | MS Logística (nuevo) |
| `DEC-` | Decisión técnica global |

## Convención al documentar

Al cerrar una sesión en un módulo:

1. **Módulo:** entrada en su `avances.md`; si hubo bug → su `errores.md`; si hubo fix → su `arreglos-y-cambios.md`.
2. **Central:** resumen en `document/avances.md` con **Origen:** `EMG` / `FE` / etc.; errores/arreglos globales en `document/errores.md` y `document/arreglos-y-cambios.md`.
3. **Progreso:** actualizar `document/progreso.md` y checklists en `document/plan-de-implementacion.md`.
