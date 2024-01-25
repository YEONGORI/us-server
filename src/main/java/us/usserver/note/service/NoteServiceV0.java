package us.usserver.note.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.author.Author;
import us.usserver.global.EntityService;
import us.usserver.like.paragraph.ParagraphLike;
import us.usserver.like.paragraph.ParagraphLikeRepository;
import us.usserver.note.NoteService;
import us.usserver.note.dto.ParagraphPreview;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.ParagraphRepository;
import us.usserver.vote.Vote;
import us.usserver.vote.VoteRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NoteServiceV0 {
    private final EntityService entityService;

    private final ParagraphRepository paragraphRepository;
    private final VoteRepository voteRepository;
    private final ParagraphLikeRepository paragraphLikeRepository;

    public List<ParagraphPreview> wroteParagraphs(Long authorId) {
        Author author = entityService.getAuthor(authorId);

        List<Paragraph> paragraphs = paragraphRepository.findAllByAuthor(author);
        return paragraphs.stream().map(paragraph -> ParagraphPreview.fromParagraph(
                paragraph,
                paragraph.getChapter().getNovel(),
                paragraph.getChapter()
        )).toList();
    }

    public List<ParagraphPreview> votedParagraphs(Long authorId) {
        Author author = entityService.getAuthor(authorId);

        List<Vote> votes = voteRepository.findAllByAuthor(author);
        return votes.stream().map(vote -> ParagraphPreview.fromParagraph(
                vote.getParagraph(),
                vote.getParagraph().getChapter().getNovel(),
                vote.getParagraph().getChapter()
        )).toList();
    }

    public List<ParagraphPreview> likedParagraphs(Long authorId) {
        Author author = entityService.getAuthor(authorId);

        List<ParagraphLike> paragraphLikes = paragraphLikeRepository.findAllByAuthor(author);
        return paragraphLikes.stream().map(paragraphLike -> ParagraphPreview.fromParagraph(
                paragraphLike.getParagraph(),
                paragraphLike.getParagraph().getChapter().getNovel(),
                paragraphLike.getParagraph().getChapter()
        )).toList();
    }
}
