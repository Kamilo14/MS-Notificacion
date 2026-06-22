const TITLES = {
  STOCK_CRITICO: 'Alerta: stock critico',
  DONACION_CONFIRMADA: 'Confirmacion de donacion',
  TRANSFERENCIA_CREADA: 'Transferencia creada',
  TRANSFERENCIA_ESTADO: 'Actualizacion de transferencia',
  MISION_ASIGNADA: 'Mision asignada',
  EMERGENCIA_CREADA: 'Emergencia declarada',
  ANUNCIO_PUBLICADO: 'Anuncio urgente',
  NECESIDAD_CREADA: 'Nueva necesidad',
};

export function buildEmailContent(message) {
  const tipo = message.tipo ?? 'NOTIFICACION';
  const subject = message.titulo ?? TITLES[tipo] ?? 'Notificacion CatastrofesCL';
  const text = message.cuerpo ?? 'Tiene una nueva notificacion en CatastrofesCL.';
  const html = `
    <html>
      <body style="font-family: Arial, sans-serif;">
        <h2>${subject}</h2>
        <p>${text}</p>
        <hr />
        <small>Tipo: ${tipo} | Evento: ${message.routingKey ?? 'n/a'}</small>
      </body>
    </html>
  `;
  return { subject, text, html };
}
