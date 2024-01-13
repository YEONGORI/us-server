package us.usserver.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import us.usserver.notification.Enum.NotificationMessage;
import us.usserver.notification.NotificationService;
import us.usserver.notification.dto.NotificationInfo;

@Slf4j
@Aspect
@Component
@EnableAsync
@RequiredArgsConstructor
public class NotifyAspect {
    private final NotificationService notificationService;

    @Pointcut("@annotation(us.usserver.global.annotation.Notify)")
    public void annotationPointcut() {}

    @Async
    @AfterReturning(pointcut = "annotationPointcut()", returning = "notification")
    public void checkValue(JoinPoint joinPoint, Notification notification) throws Throwable {
        notificationService.send(
                notification.getReceiver(),
                notification.getNotificationType(),
                notification.getContent(),
                notification.getUrl()
        );
        log.info("Pointcut Result = {}", notification);
    }
}
