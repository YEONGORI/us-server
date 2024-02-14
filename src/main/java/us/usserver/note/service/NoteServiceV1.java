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
import us.usserver.note.dto.GetParagraphNote;
import us.usserver.note.dto.ParagraphPreview;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.ParagraphRepository;
import us.usserver.vote.Vote;
import us.usserver.vote.repository.VoteJpaRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NoteServiceV1 implements NoteService {
    private final EntityService entityService;

    private final ParagraphRepository paragraphRepository;
    private final VoteJpaRepository voteJpaRepository;
    private final ParagraphLikeRepository paragraphLikeRepository;

    @Override
    public GetParagraphNote wroteParagraphs(Long authorId) {
        Author author = entityService.getAuthor(authorId);

        List<Paragraph> paragraphs = paragraphRepository.findAllByAuthor(author);
        List<ParagraphPreview> paragraphPreviews = paragraphs.stream().map(paragraph -> ParagraphPreview.fromParagraph(
                paragraph,
                paragraph.getChapter().getNovel(),
                paragraph.getChapter()
        )).toList();

        return GetParagraphNote.builder().paragraphPreviews(paragraphPreviews).build();
    }

    @Override
    public GetParagraphNote votedParagraphs(Long authorId) {
        Author author = entityService.getAuthor(authorId);

        List<Vote> votes = voteJpaRepository.findAllByAuthor(author);
        List<ParagraphPreview> paragraphPreviews = votes.stream().map(vote -> ParagraphPreview.fromParagraph(
                vote.getParagraph(),
                vote.getParagraph().getChapter().getNovel(),
                vote.getParagraph().getChapter()
        )).toList();

        return GetParagraphNote.builder().paragraphPreviews(paragraphPreviews).build();
    }

    @Override
    public GetParagraphNote likedParagraphs(Long authorId) {
        Author author = entityService.getAuthor(authorId);

        List<ParagraphLike> paragraphLikes = paragraphLikeRepository.findAllByAuthor(author);
        List<ParagraphPreview> paragraphPreviews = paragraphLikes.stream().map(paragraphLike -> ParagraphPreview.fromParagraph(
                paragraphLike.getParagraph(),
                paragraphLike.getParagraph().getChapter().getNovel(),
                paragraphLike.getParagraph().getChapter()
        )).toList();

        return GetParagraphNote.builder().paragraphPreviews(paragraphPreviews).build();
    }
}
