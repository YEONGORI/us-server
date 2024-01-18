package us.usserver.chapter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.chapter.dto.ChapterDetailInfo;
import us.usserver.global.ApiCsResponse;
import us.usserver.global.ApiCsResponse2;
import us.usserver.global.annotation.Notify;
import us.usserver.notification.Enum.NotificationType;
import us.usserver.notification.dto.NotificationInfo;
import us.usserver.notification.dto.NotificationMessage;


@ResponseBody
@RestController
@RequestMapping("/chapter")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @GetMapping("/{novelId}/{chapterId}")
    public ResponseEntity<ApiCsResponse<?>> getChapterDetailInfo(
            @PathVariable Long novelId,
            @PathVariable Long chapterId
    ) {
        Long authorId = 1L; // TODO 토큰으로 교체 예정
        ChapterDetailInfo chapterDetailInfo = chapterService.getChapterDetailInfo(novelId, authorId, chapterId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(chapterDetailInfo)
                .build();
        return ResponseEntity.ok(response);
    }

    // TODO: 이전 회차의 권한 설정을 가져올 수 있는 기능을 제공 해야 하는데, 이 권한을 저장 하는 엔티티가 없어서 생성 해야함
    @Notify
    @PostMapping("/{novelId}")
    public ResponseEntity<ApiCsResponse2<?>> createChapter(
            @PathVariable Long novelId
    ) {
        Long authorId = 1L; // TODO: 토큰에서 가져올 예정

        Chapter chapter = chapterService.createChapter(novelId, authorId);

        NotificationInfo message = NotificationInfo.builder()
                .receiverId(authorId)
                .novelId(novelId)
                .content(chapter.getPart() + "화가 업데이트 되었습니다.")
                .url("http://localhost:8080/novel/" + novelId)
                .type(NotificationType.UPDATED)
                .build();

        ApiCsResponse2<Object> response = ApiCsResponse2.builder()
                .status(HttpStatus.CREATED.value())
                .message(message)
                .data("null")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
