package us.usserver.novel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NovelRepository extends JpaRepository<Novel, Long> {
    Optional<Novel> getNovelById(Long id);
}
