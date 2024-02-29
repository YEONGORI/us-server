package us.usserver.domain.novel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import us.usserver.domain.member.dto.AuthorInfo;
import us.usserver.domain.novel.Novel;
import us.usserver.domain.novel.constant.Genre;
import us.usserver.domain.novel.constant.Hashtag;

import java.util.Set;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NovelInfo {
    @Schema(description = "소설 ID", example = "1")
    private Long id;

    @Schema(description = "소설 제목", example = "주술 회전")
    private String title;

    @Schema(description = "소설 장르", example = "FANTASY")
    private Genre genre;

    @Schema(description = "해시태그",example = "[HASHTAG1, HASHTAG2, ...]")
    private Set<Hashtag> hashtag;

    @Schema(description = "메인 작가 이름",example = "강아지상")
    private AuthorInfo createdAuthor;

    @Schema(description = "참여 작가 수",example = "12")
    private Integer joinedAuthorCnt;

    @Schema(description = "댓글 갯수",example = "899")
    private Integer commentCnt;

    @Schema(description = "좋아요 갯수",example = "998")
    private Integer likeCnt;

    @Schema(description = "소설 공유 url",example = "https:// ~ ~")
    private String novelSharelUrl;


    public static NovelInfo mapNovelToNovelInfo(Novel novel) {
        return NovelInfo.builder()
                .id(novel.getId())
                .title(novel.getTitle())
                .genre(novel.getGenre())
                .hashtag(novel.getHashtags())
                .createdAuthor(AuthorInfo.fromAuthor(novel.getMainAuthor()))
                .joinedAuthorCnt(novel.getAuthorities().size()) // TODO: 여기 size만 필요한데 이거 유지 관리 하는 필드가 필요함
                .commentCnt(novel.getComments().size())
                .likeCnt(novel.getNovelLikes().size())
                .novelSharelUrl("")
                .build();
    }
}
