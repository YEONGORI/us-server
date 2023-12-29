package us.usserver.bookshelf.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.author.Author;
import us.usserver.bookshelf.BookshelfService;
import us.usserver.bookshelf.dto.NovelPreview;
import us.usserver.global.EntityService;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookshelfServiceV0 implements BookshelfService {
    private final EntityService entityService;

    @Override
    public List<NovelPreview> recentViewedNovels(Long authorId) {
        Author author = entityService.getAuthor(authorId);
    }

    @Override
    public List<NovelPreview> createdNovels(Long authorId) {
        return null;
    }

    @Override
    public List<NovelPreview> joinedNovels(Long authorId) {
        return null;
    }

    @Override
    public List<NovelPreview> likedNovels(Long authorId) {
        return null;
    }
}
