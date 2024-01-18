package us.usserver.notification.dto;

import lombok.*;
import us.usserver.notification.Enum.NotificationType;
import us.usserver.notification.Notification;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NotificationInfo {
    private Long receiverId;
    private Long novelId;
    private String content;
    private String url;
    private NotificationType type;
}
