package us.usserver.author;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import us.usserver.author.dto.UpdateAuthorReq;
import us.usserver.global.ApiCsResponse;
import us.usserver.member.Member;


@RequiredArgsConstructor
@RequestMapping("/author")
@RestController
public class AuthorController {
    private final AuthorService authorService;

    @PatchMapping
    public ResponseEntity<ApiCsResponse<?>> updateAuthor(@AuthenticationPrincipal Member member,
                                                         @ModelAttribute UpdateAuthorReq updateAuthorReq) {
        authorService.updateAuthor(member.getId(), updateAuthorReq);
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}