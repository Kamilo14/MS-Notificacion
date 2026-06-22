CREATE TABLE IF NOT EXISTS preferencias_notificacion (
    id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id  uuid        NOT NULL,
    tipo_evento varchar(50) NOT NULL,
    in_app      boolean     NOT NULL DEFAULT true,
    push        boolean     NOT NULL DEFAULT true,
    correo      boolean     NOT NULL DEFAULT false,
    CONSTRAINT uq_preferencias_usuario_tipo UNIQUE (usuario_id, tipo_evento)
);

CREATE INDEX IF NOT EXISTS idx_preferencias_usuario
    ON preferencias_notificacion (usuario_id);
