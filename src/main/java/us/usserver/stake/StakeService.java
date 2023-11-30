package us.usserver.stake;

import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.novel.Novel;
import us.usserver.stake.dto.StakeInfo;

import java.util.List;

@Service
public interface StakeService {
    // Q: 이런 경우에는 novelId를 받아서 set 메서드 내에서 Novel을 조회하는 것이 옳은가? 아니면 Novel을 받는 것이 옳은가?
    // A: Novel을 전달하는 것이 불필요한 쿼리를 생성 하지 않기 때문에 더 효율적이다.
    void setStakeInfoOfNovel(Novel novel);

    List<StakeInfo> getStakeInfoOfNovel(Long novelId);
}
