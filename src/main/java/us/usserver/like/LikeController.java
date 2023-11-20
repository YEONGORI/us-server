package us.usserver.like;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.ApiResponse;
import us.usserver.like.dto.SetLikeReq;
import us.usserver.like.likeEnum.LikeType;

@ResponseBody
@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> setLike(@RequestBody SetLikeReq likeReq) {
        LikeType likeType = likeReq.getLikeType();
        Long likeId = likeReq.getId();

        if (likeType == LikeType.NOVEL) {
            likeService.setNovelLike(likeId);
        } else if (likeType == LikeType.PARAGRAPH) {
            likeService.setParagraphLike(likeId);
        } else if (likeType == LikeType.CHAPTER_COMMENT) {
            likeService.setChatperCommentLike(likeId);
        } else {
            // TODO: 소설 댓글 좋아요, 회차 댓글 좋아요 추가 예정
        }

        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}
