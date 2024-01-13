package us.usserver.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.notification.Notification;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationInfo {
    private Long id;
    private Long memberId;
    private String content;
    private String type;
    private String createdAt;

    public static NotificationInfo fromNotification(Notification notification) {
        return NotificationInfo.builder()
                .id(notification.getId())
                .memberId(notification.getReceiver().getId())
                .content(notification.getContent())
                .type(notification.getNotificationType().toString())
                .createdAt(notification.getCreatedAt().toString())
                .build();
    }
}
