package us.usserver.comment.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.chapter.Chapter;
import us.usserver.comment.Comment;
import us.usserver.comment.CommentRepository;
import us.usserver.comment.CommentService;
import us.usserver.comment.dto.CommentContent;
import us.usserver.comment.dto.CommentInfo;
import us.usserver.global.EntityService;
import us.usserver.novel.Novel;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceV0 implements CommentService {
    private final EntityService entityService;
    private final CommentRepository commentRepository;

    @Override
    public List<CommentInfo> getCommentsOfNovel(Long novelId) {
        Novel novel = entityService.getNovel(novelId);
        List<Comment> commentsOfNovel = commentRepository.findAllByNovel(novel);

        String novelTitle = novel.getTitle();
        return commentsOfNovel.stream()
                .map(comment -> CommentInfo.fromComment(comment, novelTitle))
                .toList();
    }

    @Override
    public List<CommentInfo> getCommentsOfChapter(Long chapterId) {
        Chapter chapter = entityService.getChapter(chapterId);
        List<Comment> commentsOfChapter = commentRepository.findAllByChapter(chapter);

        String chapterTitle = chapter.getTitle();
        return commentsOfChapter.stream()
                .map(comment -> CommentInfo.fromComment(comment, chapterTitle))
                .toList();
    }

    @Override
    public CommentInfo writeCommentOnNovel(Long novelId, Long authorId, CommentContent commentContent) {
        Author author = entityService.getAuthor(authorId);
        Novel novel = entityService.getNovel(novelId);

        return CommentInfo.fromComment(
                commentRepository.save(Comment.builder()
                    .content(commentContent.getContent())
                    .author(author)
                    .novel(novel)
                    .chapter(null)
                    .build()),
                novel.getTitle());
    }

    @Override
    public CommentInfo writeCommentOnChapter(Long chapterId, Long authorId, CommentContent commentContent) {
        Author author = entityService.getAuthor(authorId);
        Chapter chapter = entityService.getChapter(chapterId);
        Novel novel = chapter.getNovel();

        return CommentInfo.fromComment(
                commentRepository.save(Comment.builder()
                        .content(commentContent.getContent())
                        .author(author)
                        .novel(novel)
                        .chapter(chapter)
                        .build()),
                chapter.getTitle());
    }

    @Override
    public List<CommentInfo> getCommentsByAuthor(Long authorId) {
        Author author = entityService.getAuthor(authorId);
        List<Comment> commentsByAuthor = commentRepository.findAllByAuthor(author);

        return commentsByAuthor.stream()
                .map(comment -> CommentInfo
                        .fromComment(
                                comment,
                                comment.getChapter() == null ? comment.getNovel().getTitle() : comment.getChapter().getTitle()
                        ))
                .toList();
    }
}
