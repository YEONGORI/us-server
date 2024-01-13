package us.usserver.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import us.usserver.notification.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
