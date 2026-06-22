CREATE TABLE IF NOT EXISTS notificaciones (
    id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id  uuid         NOT NULL,
    tipo        varchar(50)  NOT NULL,
    titulo      varchar(200) NOT NULL,
    cuerpo      text         NOT NULL,
    canal       varchar(20)  NOT NULL,
    leida       boolean      NOT NULL DEFAULT false,
    metadata    jsonb,
    creada_en   timestamptz  NOT NULL DEFAULT now(),
    leida_en    timestamptz
);

CREATE INDEX IF NOT EXISTS idx_notificaciones_usuario_creada
    ON notificaciones (usuario_id, creada_en DESC);

CREATE INDEX IF NOT EXISTS idx_notificaciones_usuario_leida
    ON notificaciones (usuario_id, leida);
