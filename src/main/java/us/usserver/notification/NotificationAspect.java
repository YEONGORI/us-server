package us.usserver.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import us.usserver.global.CustomApiResponse;
import us.usserver.notification.dto.NotificationInfo;

@Slf4j
@Aspect
@Component
@EnableAsync
@EnableWebMvc
@RequiredArgsConstructor
public class NotificationAspect {
    private final NotificationService notificationService;

    @Pointcut("@annotation(us.usserver.global.annotation.Notify)")
    public void annotationPointcut() {}

    @Async
    @AfterReturning(pointcut = "annotationPointcut()", returning = "responseEntity")
    public void checkValue(JoinPoint joinPoint, ResponseEntity<CustomApiResponse<?>> responseEntity) {

        CustomApiResponse<?> apiResponse = responseEntity.getBody();

        NotificationInfo notificationInfo = apiResponse.getNotificationInfo();
        notificationService.send(
                notificationInfo.getReceiversId(),
                notificationInfo.getTitle(),
                notificationInfo.getContent(),
                notificationInfo.getUrl(),
                notificationInfo.getType()
        );

        log.info("Pointcut Result = {}", notificationInfo);
    }
}
