package us.usserver.domain.comment.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.domain.comment.entity.Comment;
import us.usserver.domain.comment.repository.CommentJpaRepository;
import us.usserver.domain.comment.dto.CommentContent;
import us.usserver.domain.comment.dto.CommentInfo;
import us.usserver.global.EntityFacade;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.AuthorNotAuthorizedException;
import us.usserver.global.exception.CommentLengthOutOfRangeException;
import us.usserver.domain.novel.entity.Novel;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceV0 {
    private final EntityFacade entityFacade;
    private final CommentJpaRepository commentJpaRepository;

    public List<CommentInfo> getCommentsOfNovel(Long novelId) {
        Novel novel = entityFacade.getNovel(novelId);
        List<Comment> commentsOfNovel = commentJpaRepository.findAllByNovel(novel);

        String novelTitle = novel.getTitle();
        return commentsOfNovel.stream()
                .map(comment -> CommentInfo.fromComment(comment, novelTitle, comment.getCommentLikes().size()))
                .toList();
    }

    public List<CommentInfo> getCommentsOfChapter(Long chapterId) {
        Chapter chapter = entityFacade.getChapter(chapterId);
        List<Comment> commentsOfChapter = commentJpaRepository.findAllByChapter(chapter);

        String chapterTitle = chapter.getTitle();
        return commentsOfChapter.stream()
                .map(comment -> CommentInfo.fromComment(comment, chapterTitle, comment.getCommentLikes().size()))
                .toList();
    }

    public CommentInfo writeCommentOnNovel(Long novelId, Long authorId, CommentContent commentContent) {
        Author author = entityFacade.getAuthor(authorId);
        Novel novel = entityFacade.getNovel(novelId);
        Integer ZeroLikeCnt = 0;

        if (commentContent.getContent().isEmpty() || commentContent.getContent().length() > 300) {
            throw new CommentLengthOutOfRangeException(ExceptionMessage.COMMENT_LENGTH_OUT_OF_RANGE);
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

    public CommentInfo writeCommentOnChapter(Long chapterId, Long authorId, CommentContent commentContent) {
        Author author = entityFacade.getAuthor(authorId);
        Chapter chapter = entityFacade.getChapter(chapterId);
        Novel novel = chapter.getNovel();
        Integer ZeroLikeCnt = 0;

        if (commentContent.getContent().isEmpty() || commentContent.getContent().length() > 300) {
            throw new CommentLengthOutOfRangeException(ExceptionMessage.COMMENT_LENGTH_OUT_OF_RANGE);
        }

        Comment comment = commentJpaRepository.save(Comment.builder()
                .content(commentContent.getContent())
                .author(author)
                .novel(novel)
                .chapter(chapter)
                .build());
        novel.getComments().add(comment);

        return CommentInfo.fromComment(
                comment,
                chapter.getTitle(),
                ZeroLikeCnt
        );
    }

    public List<CommentInfo> getCommentsByAuthor(Long authorId) {
        Author author = entityFacade.getAuthor(authorId);
        List<Comment> commentsByAuthor = commentJpaRepository.findAllByAuthor(author);

        return commentsByAuthor.stream()
                .map(comment -> CommentInfo
                        .fromComment(
                                comment,
                                comment.getChapter() == null ? comment.getNovel().getTitle() : comment.getChapter().getTitle(),
                                comment.getCommentLikes().size()
                        ))
                .toList();
    }

    public void deleteComment(Long commentId, Long authorId) {
        Comment comment = entityFacade.getComment(commentId);
        Author author = entityFacade.getAuthor(authorId);

        if (!comment.getAuthor().getId().equals(author.getId())) {
            throw new AuthorNotAuthorizedException(ExceptionMessage.AUTHOR_NOT_AUTHORIZED);
        }

        commentJpaRepository.delete(comment);
    }
}
