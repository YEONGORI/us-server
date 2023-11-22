package us.usserver.stake;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import us.usserver.novel.Novel;
import us.usserver.stake.dto.StakeInfo;

import java.util.List;

@Repository
public interface StakeRepository extends JpaRepository<Stake, Long> {

    @Query("SELECT (s.author, s.percentage) FROM Stake s WHERE s.novel = :novel")
    List<StakeInfo> findAllByNovel(Novel novel);
}
