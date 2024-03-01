package us.usserver.domain.paragraph.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.paragraph.repository.ParagraphLikeRepository;
import us.usserver.domain.paragraph.entity.Paragraph;
import us.usserver.global.EntityFacade;
import us.usserver.global.exception.DuplicatedLikeException;
import us.usserver.domain.paragraph.entity.ParagraphLike;

@Service
@Transactional
@RequiredArgsConstructor
public class ParagraphLikeServiceImpl implements ParagraphLikeService {
    private final EntityFacade entityFacade;
    private final ParagraphLikeRepository paragraphLikeRepository;

    @Override
    public void setParagraphLike(Long paragraphId, Long authorId) {
        Paragraph paragraph = entityFacade.getParagraph(paragraphId);
        Author author = entityFacade.getAuthor(authorId);

        paragraphLikeRepository.findFirstByParagraphAndAuthor(paragraph, author)
                .ifPresent(paragraphLike -> {
                    throw new DuplicatedLikeException("PARAGRAPH_LIKE");
                });

        ParagraphLike paragraphLike = ParagraphLike.builder()
                .paragraph(paragraph)
                .author(author)
                .build();
        paragraphLikeRepository.save(paragraphLike);
    }

    @Override
    public void deleteParagraphLike(Long paragraphId, Long authorId) {
        Paragraph paragraph = entityFacade.getParagraph(paragraphId);
        Author author = entityFacade.getAuthor(authorId);

        paragraphLikeRepository.findFirstByParagraphAndAuthor(paragraph, author)
                .ifPresent(paragraphLikeRepository::delete);
    }
}
