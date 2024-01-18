package us.usserver.vote;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.global.ApiCsResponse;
import us.usserver.global.ApiCsResponse2;
import us.usserver.global.annotation.Notify;
import us.usserver.notification.Enum.NotificationType;
import us.usserver.notification.dto.NotificationInfo;
import us.usserver.paragraph.dto.ParagraphInVoting;

import java.util.List;

@ResponseBody
@RestController
@RequestMapping("/vote")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @Notify
    @PostMapping("/{paragraphId}")
    public ResponseEntity<ApiCsResponse2<?>> voting(@PathVariable Long paragraphId) {
        Long authorId = 0L;
        Vote vote = voteService.voting(paragraphId, authorId);

        NotificationInfo message = NotificationInfo.builder()
                .receiverId(vote.getParagraph().getAuthor().getId())
                .novelId(vote.getParagraph().getChapter().getNovel().getId())
                .content(vote.getAuthor().getNickname() + "님이 작가님의 소설에 투표했습니다.")
                .url("http://localhost:8080/novel/" + vote.getParagraph().getChapter().getNovel().getId())
                .type(NotificationType.VOTED)
                .build();

        ApiCsResponse2<Object> response = ApiCsResponse2.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(message)
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{voteId}")
    public ResponseEntity<ApiCsResponse<?>> cancelVote(@PathVariable Long voteId) {
        Long authorId = 0L;
        voteService.unvoting(voteId, authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}
