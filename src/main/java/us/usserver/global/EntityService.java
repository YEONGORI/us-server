package us.usserver.global;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterRepository;
import us.usserver.comment.Comment;
import us.usserver.comment.CommentRepository;
import us.usserver.global.exception.*;
import us.usserver.novel.Novel;
import us.usserver.novel.repository.NovelJpaRepository;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.ParagraphRepository;
import us.usserver.vote.Vote;
import us.usserver.vote.VoteRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntityService {
    private final AuthorRepository authorRepository;
    private final NovelJpaRepository novelJpaRepository;
    private final ChapterRepository chapterRepository;
    private final ParagraphRepository paragraphRepository;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;

    public Author getAuthor(Long authorId) {
        Optional<Author> authorById = authorRepository.getAuthorById(authorId);
        if (authorById.isEmpty()) {
            throw new AuthorNotFoundException(ExceptionMessage.Author_NOT_FOUND);
        }
        return authorById.get();
    }

    public Novel getNovel(Long novelId) {
        Optional<Novel> novelById = novelJpaRepository.getNovelById(novelId);
        if (novelById.isEmpty()) {
            throw new NovelNotFoundException(ExceptionMessage.Novel_NOT_FOUND);
        }
        return novelById.get();
    }

    public Chapter getChapter(Long chapterId) {
        Optional<Chapter> chapterById = chapterRepository.getChapterById(chapterId);
        if (chapterById.isEmpty()) {
            throw new ChapterNotFoundException(ExceptionMessage.Chapter_NOT_FOUND);
        }
        return chapterById.get();
    }

    public Paragraph getParagraph(Long paragraphId) {
        Optional<Paragraph> paragraphById = paragraphRepository.getParagraphById(paragraphId);
        if (paragraphById.isEmpty()) {
            throw new ParagraphNotFoundException(ExceptionMessage.Paragraph_NOT_FOUND);
        }
        return paragraphById.get();
    }

    public Comment getComment(Long commentId) {
        Optional<Comment> commentById = commentRepository.getCommentById(commentId);
        if (commentById.isEmpty()) {
            throw new CommentNotFoundException(ExceptionMessage.Comment_NOT_FOUND);
        }
        return commentById.get();
    }

    public Vote getVote(Long voteId) {
        Optional<Vote> voteById = voteRepository.getVoteById(voteId);
        if (voteById.isEmpty()) {
            throw new VoteNotFoundException(ExceptionMessage.Vote_NOT_FOUND);
        }
        return voteById.get();
    }
}
