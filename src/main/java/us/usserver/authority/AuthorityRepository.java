package us.usserver.authority;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.author.Author;
import us.usserver.novel.Novel;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Integer countAllByNovel(Novel novel);

    List<Authority> findAllByAuthor(Author author);

    List<Authority> findAllByNovel(Novel novel);

}
