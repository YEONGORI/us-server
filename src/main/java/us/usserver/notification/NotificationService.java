package us.usserver.notification;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import us.usserver.author.Author;
import us.usserver.member.Member;
import us.usserver.notification.Enum.NotificationType;

@Service
public interface NotificationService {
    SseEmitter subscribe(Long memberId, Long lastEventId);

    Long makeTimeIncludeId(Long memberId);

    void sendNotification(SseEmitter emitter, Long eventId, Long emitterId, Object data);

    boolean hasLostData(Long lastEventId);

    void sendLostData(SseEmitter emitter, Long lastEventId, Long memberId, Long emitterId);

    void send(Long receiverId, Long titleId, String content, String url, NotificationType notificationType);

    Notification createNotification(Author receiver, NotificationType notificationType, String title, String content, String url);
}
