package us.usserver.comment.novel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.comment.novel.NoComment;
import us.usserver.comment.novel.NoCommentRepository;
import us.usserver.comment.novel.NoCommentService;
import us.usserver.comment.novel.dto.CommentsInNovelRes;
import us.usserver.comment.novel.dto.PostCommentReq;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoCommentServiceV0 implements NoCommentService {
    private final NovelRepository novelRepository;
    private final AuthorRepository authorRepository;
    private final NoCommentRepository noCommentRepository;

    @Override
    public List<CommentsInNovelRes> getCommentsInNovel(Long novelId) {
        Novel novel = getNovel(novelId);
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
        Novel novel = getNovel(novelId);
        Author author = getAuthor(authorId);

        noCommentRepository.save(NoComment.builder()
                .content(postCommentReq.getContent())
                .novel(novel)
                .author(author)
                .build());
        return getCommentsInNovel(novelId);
    }

    private Novel getNovel(Long novelId) {
        Optional<Novel> novelById = novelRepository.getNovelById(novelId);
        if (novelById.isEmpty()) {
            log.info(ExceptionMessage.Novel_NOT_FOUND);
            throw new NovelNotFoundException(ExceptionMessage.Novel_NOT_FOUND);
        }
        return novelById.get();
    }

    private Author getAuthor(Long authorId) {
        Optional<Author> authorById = authorRepository.getAuthorById(authorId);
        if (authorById.isEmpty()) {
            throw new AuthorNotFoundException(ExceptionMessage.Author_NOT_FOUND);
        }
        return authorById.get();
    }
}
