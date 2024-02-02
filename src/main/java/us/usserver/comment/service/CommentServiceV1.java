package us.usserver.comment.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.author.Author;
import us.usserver.chapter.Chapter;
import us.usserver.comment.Comment;
import us.usserver.comment.repository.CommentJpaRepository;
import us.usserver.comment.CommentService;
import us.usserver.comment.dto.CommentContent;
import us.usserver.comment.dto.CommentInfo;
import us.usserver.comment.dto.GetCommentResponse;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.AuthorNotAuthorizedException;
import us.usserver.global.exception.CommentLengthOutOfRangeException;
import us.usserver.novel.Novel;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceV1 implements CommentService {
    private final EntityService entityService;
    private final CommentJpaRepository commentJpaRepository;

    @Override
    public GetCommentResponse getCommentsOfNovel(Long novelId) {
        Novel novel = entityService.getNovel(novelId);
        List<Comment> commentsOfNovel = commentJpaRepository.findAllByNovel(novel);

        String novelTitle = novel.getTitle();
        List<CommentInfo> commentInfos = commentsOfNovel.stream()
                .map(comment -> CommentInfo.fromComment(comment, novelTitle, comment.getCommentLikes().size()))
                .toList();

        return GetCommentResponse.builder().commentInfos(commentInfos).build();
    }

    @Override
    public GetCommentResponse getCommentsOfChapter(Long chapterId) {
        Chapter chapter = entityService.getChapter(chapterId);
        List<Comment> commentsOfChapter = commentJpaRepository.findAllByChapter(chapter);

        String chapterTitle = chapter.getTitle();
        List<CommentInfo> commentInfos = commentsOfChapter.stream()
                .map(comment -> CommentInfo.fromComment(comment, chapterTitle, comment.getCommentLikes().size()))
                .toList();

        return GetCommentResponse.builder().commentInfos(commentInfos).build();
    }

    @Override
    public CommentInfo writeCommentOnNovel(Long novelId, Long authorId, CommentContent commentContent) {
        Author author = entityService.getAuthor(authorId);
        Novel novel = entityService.getNovel(novelId);
        Integer ZeroLikeCnt = 0;

        if (commentContent.getContent().isEmpty() || commentContent.getContent().length() > 300) {
            throw new CommentLengthOutOfRangeException(ExceptionMessage.Comment_Length_OUT_OF_RANGE);
        }

        Comment comment = commentJpaRepository.save(Comment.builder()
                .content(commentContent.getContent())
                .author(author)
                .novel(novel)
                .chapter(null)
                .build());
        novel.getComments().add(comment);

        return CommentInfo.fromComment(comment, novel.getTitle(), ZeroLikeCnt);
    }

    @Override
    public CommentInfo writeCommentOnChapter(Long chapterId, Long authorId, CommentContent commentContent) {
        Author author = entityService.getAuthor(authorId);
        Chapter chapter = entityService.getChapter(chapterId);
        Novel novel = chapter.getNovel();
        Integer ZeroLikeCnt = 0;

        if (commentContent.getContent().isEmpty() || commentContent.getContent().length() > 300) {
            throw new CommentLengthOutOfRangeException(ExceptionMessage.Comment_Length_OUT_OF_RANGE);
        }

        Comment comment = commentJpaRepository.save(Comment.builder()
                .content(commentContent.getContent())
                .author(author)
                .novel(novel)
                .chapter(chapter)
                .build());
        novel.getComments().add(comment);
        chapter.getComments().add(comment);

        return CommentInfo.fromComment(
                comment,
                chapter.getTitle(),
                ZeroLikeCnt
        );
    }

    @Override
    public GetCommentResponse getCommentsByAuthor(Long authorId) {
        Author author = entityService.getAuthor(authorId);
        List<Comment> commentsByAuthor = commentJpaRepository.findAllByAuthor(author);

        List<CommentInfo> commentInfos = commentsByAuthor.stream()
                .map(comment -> CommentInfo
                        .fromComment(
                                comment,
                                comment.getChapter() == null ? comment.getNovel().getTitle() : comment.getChapter().getTitle(),
                                comment.getCommentLikes().size()
                        ))
                .toList();

        return GetCommentResponse.builder().commentInfos(commentInfos).build();
    }

    @Override
    public void deleteComment(Long commentId, Long authorId) {
        Comment comment = entityService.getComment(commentId);
        Author author = entityService.getAuthor(authorId);

        if (!comment.getAuthor().getId().equals(author.getId())) {
            throw new AuthorNotAuthorizedException(ExceptionMessage.Author_NOT_AUTHORIZED);
        }

        commentJpaRepository.delete(comment);
    }
}
