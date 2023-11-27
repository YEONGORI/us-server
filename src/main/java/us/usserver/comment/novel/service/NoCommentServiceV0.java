package us.usserver.comment.novel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.comment.novel.NoComment;
import us.usserver.comment.novel.NoCommentRepository;
import us.usserver.comment.novel.NoCommentService;
import us.usserver.comment.novel.dto.CommentsInNovelRes;
import us.usserver.comment.novel.dto.PostCommentReq;
import us.usserver.global.EntityService;
import us.usserver.novel.Novel;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoCommentServiceV0 implements NoCommentService {
    private final EntityService entityService;
    private final NoCommentRepository noCommentRepository;

    @Override
    public List<CommentsInNovelRes> getCommentsInNovel(Long novelId) {
        Novel novel = entityService.getNovel(novelId);
        List<NoComment> comments = noCommentRepository.getAllByNovel(novel);

        return comments.stream().map(comment -> CommentsInNovelRes.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .authorName(comment.getAuthor().getNickname())
                        .createdAt(comment.getCreatedAt())
                        .updatedAt(comment.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentsInNovelRes> postCommentInNovel(Long novelId, Long authorId, PostCommentReq postCommentReq) {
        Novel novel = entityService.getNovel(novelId);
        Author author = entityService.getAuthor(authorId);

        noCommentRepository.save(NoComment.builder()
                .content(postCommentReq.getContent())
                .novel(novel)
                .author(author)
                .build());
        return getCommentsInNovel(novelId);
    }
}
