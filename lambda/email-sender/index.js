import amqp from 'amqplib';
import { SESClient, SendEmailCommand } from '@aws-sdk/client-ses';
import { buildEmailContent } from './templates.js';

const QUEUE_NAME = process.env.EMAIL_QUEUE ?? 'email.queue';
const RABBIT_URL = process.env.RABBITMQ_URL ?? 'amqp://guest:guest@localhost:5672';
const SES_FROM = process.env.SES_FROM_EMAIL ?? 'noreply@catastrofescl.cl';
const LOCAL_STUB = (process.env.EMAIL_LOCAL_STUB ?? 'true') === 'true';
const AWS_REGION = process.env.AWS_REGION ?? 'us-east-1';

const ses = LOCAL_STUB ? null : new SESClient({ region: AWS_REGION });

async function sendEmail(message) {
  const { subject, html, text } = buildEmailContent(message);

  if (LOCAL_STUB) {
    console.log('--- EMAIL STUB (local) ---');
    console.log('Para usuario:', message.usuarioId ?? '(sin usuario)');
    console.log('Asunto:', subject);
    console.log('Cuerpo:', text);
    console.log('--------------------------');
    return;
  }

  const command = new SendEmailCommand({
    Source: SES_FROM,
    Destination: {
      ToAddresses: [message.destinatarioEmail],
    },
    Message: {
      Subject: { Data: subject, Charset: 'UTF-8' },
      Body: {
        Html: { Data: html, Charset: 'UTF-8' },
        Text: { Data: text, Charset: 'UTF-8' },
      },
    },
  });

  await ses.send(command);
}

export async function handler(event) {
  if (event?.Records) {
    for (const record of event.Records) {
      const body = JSON.parse(record.body);
      await sendEmail(body);
    }
    return { status: 'ok' };
  }

  await sendEmail(event);
  return { status: 'ok' };
}

async function runLocalConsumer() {
  const connection = await amqp.connect(RABBIT_URL);
  const channel = await connection.createChannel();
  await channel.assertQueue(QUEUE_NAME, { durable: true });
  console.log(`Consumiendo ${QUEUE_NAME} (stub=${LOCAL_STUB})`);

  channel.consume(QUEUE_NAME, async (msg) => {
    if (!msg) return;
    try {
      const payload = JSON.parse(msg.content.toString());
      await sendEmail(payload);
      channel.ack(msg);
    } catch (err) {
      console.error('Error procesando email:', err);
      channel.nack(msg, false, false);
    }
  });
}

if (!process.env.AWS_LAMBDA_FUNCTION_NAME) {
  runLocalConsumer().catch((err) => {
    console.error(err);
    process.exit(1);
  });
}
