package us.usserver.domain.novel.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.domain.novel.entity.Novel;

import java.util.Optional;

@Repository
public interface NovelRepository extends JpaRepository<Novel, Long>, NovelRepositoryCustom {
    Optional<Novel> getNovelById(Long id);

    Slice<Novel> findSliceBy(Pageable pageable);
}
