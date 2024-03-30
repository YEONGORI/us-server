package us.usserver.infra.novel;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import us.usserver.domain.novel.constant.Hashtag;
import us.usserver.domain.novel.constant.NovelStatus;
import us.usserver.domain.novel.constant.Orders;
import us.usserver.domain.novel.dto.req.SearchKeyword;
import us.usserver.domain.novel.dto.SortDto;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.domain.novel.repository.NovelRepositoryCustom;

import java.util.List;
import java.util.Set;

import static org.springframework.util.StringUtils.hasText;
import static us.usserver.domain.novel.entity.QNovel.novel;

@Repository
@RequiredArgsConstructor
public class NovelRepositoryImpl implements NovelRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Novel> searchNovelList(Set<String> keywords, Pageable pageable) {
//        List<Novel> novels = getSearchNovel(keyword, pageable);
        Sort sort = pageable.getSort();
        queryFactory
                .select(novel)
                .from(novel)
                .where(
                        containsTitle(keyword),
                        containsAuthorName(keyword)
                )
//                .orderBy(pageable.getSort())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
//        return checkLastPage(pageable, novels);
        return null;
    }

//    private List<Novel> getMoreNovel(Long lastNovelId, Pageable pageable) {
//        return queryFactory
//                .select(novel)
//                .from(novel)
//                .where(ltNovelId(lastNovelId))
////                .orderBy(pageable.getSort())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize()+1)
//                .fetch();
//    }

//    private List<Novel> getSearchNovel(SearchKeyword searchKeyword, Pageable pageable) {
//        return queryFactory
//                .select(novel)
//                .from(novel)
//                .where(
//                        ltNovelId(searchKeyword.getLastNovelId()),
//                        containsTitle(searchKeyword.getTitle()),
//                        containsHashtag(searchKeyword.getHashtag()),
//                        eqNovelStatus(searchKeyword.getStatus()))
//                .orderBy(novelSort(searchKeyword.getSortDto()))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize()+1)
//                .fetch();
//    }

    private BooleanExpression containsTitle(String title) {
        return hasText(title) ? novel.title.contains(title) : null;
    }

    private BooleanExpression containsAuthorName(String name) {
        return hasText(name) ? novel.mainAuthor.nickname.contains(name) : null;
    }
//
//    private OrderSpecifier<?> getOrderSpecifier(Sort sort) {
//        sort.
//        if (sortDto == null) {
//            //Default:사전순
//            return new OrderSpecifier<>(Order.ASC, novel.title);
//        }
//
//        Order direction = (sortDto.getOrders() == Orders.DESC) ? Order.DESC : Order.ASC;
//        switch (sortDto.getSorts()) {
//            case LATEST -> {
//                //최신 업데이트순
//                return new OrderSpecifier<>(direction, novel.updatedAt);
//            }
//            case NEW -> {
//                //신작순
//                return new OrderSpecifier<>(direction, novel.createdAt);
//            }
//            default -> {
//                //조회순
//                return new OrderSpecifier<>(direction, novel.hit);
//            }
//        }
//    }
//
//    private Slice<Novel> checkLastPage(Pageable pageable, List<Novel> content) {
//        boolean hasNext = false;
//
//        if (content.size() > pageable.getPageSize()) {
//            hasNext = true;
//            content.remove(pageable.getPageSize());
//        }
//        return new SliceImpl<>(content, pageable, hasNext);
//    }
}
