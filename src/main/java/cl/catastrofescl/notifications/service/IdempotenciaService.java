package cl.catastrofescl.notifications.service;

import cl.catastrofescl.notifications.entity.EventoProcesado;
import cl.catastrofescl.notifications.repository.EventoProcesadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdempotenciaService {

    private static final String REDIS_PREFIX = "processed:";
    private static final Duration TTL_24H = Duration.ofHours(24);

    private final StringRedisTemplate redisTemplate;
    private final EventoProcesadoRepository eventoProcesadoRepository;

    @Value("${catastrofescl.rabbitmq.consumidor-nombre:ms-notifications}")
    private String nombreConsumidor;

    public boolean yaProcesado(UUID eventoId) {
        if (eventoId == null) {
            return false;
        }
        String key = REDIS_PREFIX + eventoId;
        Boolean enRedis = redisTemplate.hasKey(key);
        if (Boolean.TRUE.equals(enRedis)) {
            return true;
        }
        return eventoProcesadoRepository.existsById(eventoId);
    }

    @Transactional
    public void marcarComoProcesado(UUID eventoId) {
        if (eventoId == null) {
            return;
        }
        redisTemplate.opsForValue().set(REDIS_PREFIX + eventoId, "1", TTL_24H);
        if (!eventoProcesadoRepository.existsById(eventoId)) {
            eventoProcesadoRepository.save(EventoProcesado.builder()
                    .eventoId(eventoId)
                    .consumidor(nombreConsumidor)
                    .procesadoEn(OffsetDateTime.now())
                    .build());
        }
    }
}
