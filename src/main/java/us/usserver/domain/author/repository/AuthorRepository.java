package us.usserver.domain.author.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.domain.author.entity.Author;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> getAuthorById(Long id);
    Optional<Author> getAuthorByMemberId(Long memberId);
}
