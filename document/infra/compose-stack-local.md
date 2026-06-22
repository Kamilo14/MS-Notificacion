# Stack local — compose raíz

Archivo: `CatastrofeCL-MS/compose.yaml`

## Servicios

| Servicio | Puerto | Notas |
|----------|--------|-------|
| ms-gateway | 8080 | Entrada front |
| ms-identity | 8081 | Firebase + PG |
| ms-emergencies | 8082 | PostGIS |
| ms-logistics | 8085 | **Pendiente** en compose — JAR listo en `MSLogistica/` |
| redis | 6379 | |
| rabbitmq | 5672 / 15672 mgmt | |

## PostgreSQL

- Host desde contenedores: `host.docker.internal:5432`
- Base: **`catastrofecl`** (minúsculas)
- Variables: `.env` raíz (`POSTGRES_PASSWORD`, `MS_*_DB_URL`)

## Levantar

```powershell
cd CatastrofeCL-MS
docker compose up -d
```

## Próximo

Añadir servicio `ms-logistics` al `compose.yaml` raíz (imagen local, puerto 8085, `MS_LOGISTICS_DB_URL`, red `catastrofecl-net`). Guía: [servicios/ms-logistics-inicio.md](../servicios/ms-logistics-inicio.md).
