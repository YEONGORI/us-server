package us.usserver.domain.comment.service;


import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.domain.comment.dto.CommentContent;
import us.usserver.domain.comment.dto.CommentInfo;
import us.usserver.domain.comment.dto.GetCommentResponse;
import us.usserver.domain.comment.entity.Comment;
import us.usserver.domain.comment.repository.CommentRepository;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.global.EntityFacade;
import us.usserver.global.response.exception.BaseException;
import us.usserver.global.response.exception.ErrorCode;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final EntityFacade entityFacade;
    private final CommentRepository commentJpaRepository;

    @Override
    public GetCommentResponse getCommentsOfNovel(Long novelId) {
        Novel novel = entityFacade.getNovel(novelId);
        List<Comment> commentsOfNovel = commentJpaRepository.findAllByNovel(novel);

        String novelTitle = novel.getTitle();
        List<CommentInfo> commentInfos = commentsOfNovel.stream()
                .map(comment -> CommentInfo.fromComment(comment, novelTitle, comment.getCommentLikes().size()))
                .toList();

        return GetCommentResponse.builder().commentInfos(commentInfos).build();
    }

    @Override
    public GetCommentResponse getCommentsOfChapter(Long chapterId) {
        Chapter chapter = entityFacade.getChapter(chapterId);
        List<Comment> commentsOfChapter = commentJpaRepository.findAllByChapter(chapter);

        String chapterTitle = chapter.getTitle();
        List<CommentInfo> commentInfos = commentsOfChapter.stream()
                .map(comment -> CommentInfo.fromComment(comment, chapterTitle, comment.getCommentLikes().size()))
                .toList();

        return GetCommentResponse.builder().commentInfos(commentInfos).build();
    }

    @Override
    public CommentInfo writeCommentOnNovel(Long novelId, Long authorId, CommentContent commentContent) {
        Author author = entityFacade.getAuthor(authorId);
        Novel novel = entityFacade.getNovel(novelId);
        Integer ZeroLikeCnt = 0;

        if (commentContent.getContent().isEmpty() || commentContent.getContent().length() > 300) {
            throw new BaseException(ErrorCode.COMMENT_LENGTH_OUT_OF_RANGE);
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
        Author author = entityFacade.getAuthor(authorId);
        Chapter chapter = entityFacade.getChapter(chapterId);
        Novel novel = chapter.getNovel();
        Integer ZeroLikeCnt = 0;

        if (commentContent.getContent().isEmpty() || commentContent.getContent().length() > 300) {
            throw new BaseException(ErrorCode.COMMENT_LENGTH_OUT_OF_RANGE);
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
        Author author = entityFacade.getAuthor(authorId);
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
        Comment comment = entityFacade.getComment(commentId);
        Author author = entityFacade.getAuthor(authorId);

        if (!comment.getAuthor().getId().equals(author.getId())) {
            throw new BaseException(ErrorCode.AUTHOR_NOT_AUTHORIZED);
        }

        commentJpaRepository.delete(comment);
    }
}
