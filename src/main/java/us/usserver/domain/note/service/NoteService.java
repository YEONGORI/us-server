package us.usserver.domain.note.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.note.dto.GetParagraphNote;

@Service
public interface NoteService {
    GetParagraphNote wroteParagraphs(Long authorId);

    GetParagraphNote votedParagraphs(Long authorId);

    GetParagraphNote likedParagraphs(Long authorId);
}
