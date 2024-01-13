package us.usserver.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import us.usserver.member.Member;
import us.usserver.notification.Enum.NotificationType;
import us.usserver.notification.Notification;
import us.usserver.notification.NotificationRepository;
import us.usserver.notification.NotificationService;
import us.usserver.notification.dto.NotificationInfo;
import us.usserver.notification.repository.EmitterRepository;

import java.io.IOException;
import java.util.Map;

/**
 *
 * 알림 기능 구현 참고 블로그:
 * https://develoyummer.tistory.com/112#3.%20%F0%9F%93%8CRepository
 * https://develoyummer.tistory.com/106
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private static final long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public SseEmitter subscribe(Long memberId, Long lastEventId) {
        Long emitterId = makeTimeIncludeId(memberId);

        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        emitter.onCompletion(() -> emitterRepository.delete(emitterId));
        emitter.onTimeout(() -> emitterRepository.delete(emitterId));
        emitter.onError((e) -> emitterRepository.deleteAllEmitterStartWithId(emitterId));

        // 503 에러 방지를 위한 더미 이벤트 전송
        Long eventId = makeTimeIncludeId(memberId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [memberId = " + memberId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실 예방
        if (hasLostData(lastEventId)) {
            sendLostData(emitter, lastEventId, memberId, emitterId);
        }
        return emitter;
    }

    @Override
    public Long makeTimeIncludeId(Long memberId) {
        return memberId + System.currentTimeMillis();
    }

    @Override
    public void sendNotification(SseEmitter emitter, Long eventId, Long emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(eventId))
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            emitterRepository.delete(emitterId);
            log.error("SSE 연결 오류");
        }
    }

    @Override
    public boolean hasLostData(Long lastEventId) {
        return !lastEventId.equals(0L);
    }

    @Override
    public void sendLostData(SseEmitter emitter, Long lastEventId, Long memberId, Long emitterId) {
        Map<Long, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(memberId);
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    @Override
    @Transactional
    public void send(Member receiver, NotificationType notificationType, String content, String url) {
        Notification notification = notificationRepository.save(createNotification(receiver, notificationType, content, url));

        Long receiverId = receiver.getId();
        Long eventId = makeTimeIncludeId(receiverId);
        Map<Long, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemeberId(receiverId);

        emitters.forEach((key, emitter) -> {
            emitterRepository.saveEventCache(key, notification);
            sendNotification(emitter, eventId, key, NotificationInfo.fromNotification(notification));
        });
    }

    @Override
    public Notification createNotification(Member receiver, NotificationType notificationType, String content, String url) {
        return Notification.builder()
                .receiver(receiver)
                .notificationType(notificationType)
                .content(content)
                .url(url)
                .isRead(false)
                .build();
    }
}
