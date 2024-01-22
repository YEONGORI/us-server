package us.usserver.note;

import org.springframework.stereotype.Service;
import us.usserver.note.dto.GetParagraphNote;
import us.usserver.note.dto.ParagraphPreview;

import java.util.List;

@Service
public interface NoteService {
    GetParagraphNote wroteParagraphs(Long authorId);

    GetParagraphNote votedParagraphs(Long authorId);

    GetParagraphNote likedParagraphs(Long authorId);
}
