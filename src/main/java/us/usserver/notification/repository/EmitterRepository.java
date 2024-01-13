package us.usserver.notification.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(Long emitterId, SseEmitter sseEmitter);

    void saveEventCache(Long eventCacheId, Object event);

    Map<Long, SseEmitter> findAllEmitterStartWithByMemeberId(Long memberId);

    Map<Long, Object> findAllEventCacheStartWithByMemberId(Long memberId);

    void delete(Long emitterId);

    void deleteAllEmitterStartWithId(Long memberId);

    void deleteAllEventCacheStartWithId(Long memberId);
}
