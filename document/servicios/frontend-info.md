# Frontend info — Referencia rápida

| | |
|--|--|
| Carpeta | `frontend-info` |
| Puerto | 3000 (`npm run dev`) |
| API | Gateway `http://localhost:8080` |
| Fase plan | 8 (~50%) |

## Servicios principales

- `src/services/auth.service.ts` — register, login, sync
- `src/services/apiClient.ts` — interceptors Firebase
- `src/hooks/useEmergencies.ts` — TanStack Query

## Próximo: MS Logística (Fase 4 backend listo)

APIs vía gateway (pendiente en front): `/transferencias`, `/misiones`, `/rutas-voluntario`. Guía MS: [ms-logistics-inicio.md](./ms-logistics-inicio.md).

## Documentación del módulo (`FE`)

Carpeta: `frontend-info/document/`

| Archivo | Uso |
|---------|-----|
| `avances.md` | Bitácora del frontend |
| `errores.md` | Errores del frontend |
| `arreglos-y-cambios.md` | ARR/DEC del frontend |
| `ENDPOINTS.md` | Contratos HTTP usados |
| `flujo-autenticacion.md` | Login, registro, Firebase ↔ PostgreSQL |
| `ms-analisis/` | Análisis profundos (emergencias, etc.) |

Consolidado central: [document/avances.md](../avances.md).

## Histórico exportado

[por-servicio/avances-frontend-info.md](../por-servicio/avances-frontend-info.md)
