# MS Identidad y Acceso — Referencia rápida

| | |
|--|--|
| Carpeta | `MS-Identidad-Acceso-CatastrofeCL/MS-Identidad-y-Acceso-CatastrofeCL` |
| Puerto | 8081 / gateway `/auth/**`, `/usuarios/**` |
| BD | `catastrofecl` — Flyway tabla `flyway_schema_history_ms_identity` |
| Fase plan | 1 (✅) |

## Endpoints clave

- `POST /auth/register` — público
- `POST /auth/firebase/sync` — Bearer
- `POST /auth/firebase/sync/system` — `X-Sync-Secret`
- `GET /usuarios/yo`

## BD local

Usar base **`catastrofecl`** (minúsculas), no duplicado `CATASTROFECL`.

## Documentación del módulo (`IDN`)

Carpeta: `MS-Identidad-Acceso-CatastrofeCL/MS-Identidad-y-Acceso-CatastrofeCL/document/`

| Archivo | Uso |
|---------|-----|
| `avances.md` | Bitácora detallada de identity |
| `errores.md` | Errores del módulo |
| `arreglos-y-cambios.md` | ARR/DEC de identity |

Consolidado central: [document/errores.md](../errores.md).

## Histórico exportado

[por-servicio/avances-ms-identity.md](../por-servicio/avances-ms-identity.md)
