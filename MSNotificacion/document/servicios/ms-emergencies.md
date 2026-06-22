# MS Emergencias — Referencia rápida

| | |
|--|--|
| Carpeta | `MS-Emergencias/ms-emergencies` |
| Puerto | 8082 (directo) / 8080 vía gateway (`/emergencias/**`) |
| BD | `catastrofecl` — tablas `emergencias`, `anuncios`, `centros_acopio_emergencia` |
| Fase plan | 5 (~80%) |

## Endpoints gateway (español → inglés interno)

- `GET /emergencias/activas` → `/emergencies/active`
- `GET /emergencias/activas/geojson` → GeoJSON polígonos
- `POST /emergencias` → declarar emergencia
- `GET /anuncios` → announcements

## Documentación del módulo (`EMG`)

Carpeta: **`MS-Emergencias/ms-emergencies/`** (archivos en la raíz del repo del MS, no en subcarpeta `document/`)

| Archivo | Ruta |
|---------|------|
| `avances.md` | `MS-Emergencias/ms-emergencies/avances.md` |
| `errores.md` | `MS-Emergencias/ms-emergencies/errores.md` |
| `arreglos-y-cambios.md` | `MS-Emergencias/ms-emergencies/arreglos-y-cambios.md` |

Otros: `prueba ms-emergencies.md`, `estado_actual_ms-emergiecies.ms`.

Consolidado central: [document/errores.md](../errores.md) (p. ej. ERR-006-EMG Testcontainers).

## Histórico exportado

[por-servicio/avances-ms-emergencies.md](../por-servicio/avances-ms-emergencies.md)

## Compose

Servicio `ms-emergencies` en `CatastrofeCL-MS/compose.yaml`.
