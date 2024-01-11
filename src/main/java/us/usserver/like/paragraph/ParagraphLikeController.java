package us.usserver.like.paragraph;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.global.ApiCsResponse;

@ResponseBody
@RestController
@RequestMapping("/like/paragraph")
@RequiredArgsConstructor
public class ParagraphLikeController {
    private final ParagraphLikeService paragraphLikeService;

    @PostMapping("/{paragraphId}")
    public ResponseEntity<ApiCsResponse<?>> setLike(@PathVariable Long paragraphId) {
        Long authorId = 1L; // TODO: 유저 정보는 토큰 에서 가져올 예정

        paragraphLikeService.setParagraphLike(paragraphId, authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{paragraphId}")
    public ResponseEntity<ApiCsResponse<?>> deleteLike(@PathVariable Long paragraphId) {
        Long authorId = 1L; // TODO: 유저 정보는 토큰 에서 가져올 예정

        paragraphLikeService.deleteParagraphLike(paragraphId, authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}
