CREATE TABLE IF NOT EXISTS eventos_procesados (
    evento_id     uuid PRIMARY KEY,
    consumidor    varchar(50) NOT NULL,
    procesado_en  timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_eventos_procesados_consumidor
    ON eventos_procesados (consumidor, procesado_en DESC);
