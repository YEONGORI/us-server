package us.usserver.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository{
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<Long, Object> eventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(Long emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void saveEventCache(Long eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    @Override
    public Map<Long, SseEmitter> findAllEmitterStartWithByMemeberId(Long memberId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().equals(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<Long, Object> findAllEventCacheStartWithByMemberId(Long memberId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().equals(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void delete(Long emitterId) {
        emitters.remove(emitterId);
    }

    @Override
    public void deleteAllEmitterStartWithId(Long memberId) {
        emitters.forEach((key, emitter) -> {
            if (key.equals(memberId)) {
                emitters.remove(key);
            }
        });
    }

    @Override
    public void deleteAllEventCacheStartWithId(Long memberId) {
        eventCache.forEach((key, emitter) -> {
            if (key.equals(memberId)) {
                eventCache.remove(key);
            }
        });
    }
}
