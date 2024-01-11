package us.usserver.note;

import org.springframework.stereotype.Service;
import us.usserver.note.dto.ParagraphPreview;

import java.util.List;

@Service
public interface NoteService {
    List<ParagraphPreview> wroteParagraphs(Long authorId);

    List<ParagraphPreview> votedParagraphs(Long authorId);

    List<ParagraphPreview> likedParagraphs(Long authorId);
}
