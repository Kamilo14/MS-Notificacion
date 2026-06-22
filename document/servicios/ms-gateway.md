# MS Api Gateway — Referencia rápida

| | |
|--|--|
| Carpeta | `MS-Api-Gateway` |
| Puerto | **8080** (entrada única del front) |
| Fase plan | 0.5 (✅) |

## Front

`NEXT_PUBLIC_API_URL=http://localhost:8080`

## Firebase

- `.env` raíz: `FIREBASE_ENABLED=false` recomendado en local (identity valida Bearer).
- JSON montado en compose para identity/emergencies.

## Rutas

Ver `MS-Api-Gateway/src/main/resources/application.yml` — rewrite `/emergencias` → `/emergencies`.

## Documentación del módulo (`GW`)

Carpeta: `MS-Api-Gateway/document/`

| Archivo | Uso |
|---------|-----|
| `avances.md` | Bitácora detallada del gateway |
| `errores.md` | Errores del módulo |
| `arreglos-y-cambios.md` | ARR/DEC del gateway |

Consolidado central: [document/avances.md](../avances.md), [document/errores.md](../errores.md).

## Histórico exportado

[por-servicio/avances-ms-gateway.md](../por-servicio/avances-ms-gateway.md)
