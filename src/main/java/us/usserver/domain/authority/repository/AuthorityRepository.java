package us.usserver.domain.authority.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.domain.member.entity.Author;
import us.usserver.domain.authority.Authority;
import us.usserver.domain.novel.Novel;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Integer countAllByNovel(Novel novel);

    List<Authority> findAllByAuthor(Author author);

    List<Authority> findAllByNovel(Novel novel);

}
