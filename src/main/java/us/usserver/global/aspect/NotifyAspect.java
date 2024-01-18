package us.usserver.global.aspect;

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
import us.usserver.global.ApiCsResponse;
import us.usserver.global.ApiCsResponse2;
import us.usserver.notification.NotificationService;
import us.usserver.notification.dto.NotificationInfo;

@Slf4j
@Aspect
@EnableWebMvc
@Component
@EnableAsync
@RequiredArgsConstructor
public class NotifyAspect {
    private final NotificationService notificationService;

    @Pointcut("@annotation(us.usserver.global.annotation.Notify)")
    public void annotationPointcut() {}

    @Async
    @AfterReturning(pointcut = "annotationPointcut()", returning = "responseEntity")
    public void checkValue(JoinPoint joinPoint, ResponseEntity<ApiCsResponse2<?>> responseEntity) {
        ApiCsResponse2<?> apiCsResponse = responseEntity.getBody();

        NotificationInfo message = apiCsResponse.getMessage();

        notificationService.send(
                message.getReceiverId(),
                message.getNovelId(),
                message.getContent(),
                message.getUrl(),
                message.getType()
                );
        log.info("Pointcut Result = {}", message);
    }
}
