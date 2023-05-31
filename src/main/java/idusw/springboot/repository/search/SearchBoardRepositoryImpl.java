package idusw.springboot.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import idusw.springboot.entity.BoardEntity;
import idusw.springboot.entity.QBoardEntity;
import idusw.springboot.entity.QMemberEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Qualifier("SearchBoardRepositoryImpl")
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {
    public SearchBoardRepositoryImpl() {
        super(BoardEntity.class);
    }


    @Override
    public BoardEntity searchBoard() {
        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("--------- searchPage -------------");
        QBoardEntity boardEntity = QBoardEntity.boardEntity;
        //QReplyEntity replyEntity = QReplyEntity.replyEntity;
        QMemberEntity memberEntity = QMemberEntity.memberEntity;

        JPQLQuery<BoardEntity> jpqlQeury = from(boardEntity);
        jpqlQeury.leftJoin(memberEntity).on(boardEntity.writer.eq(memberEntity));
        //jpqlQeury.leftJoin(replyEntity).on(replyEntity.board.eq(boardEntity));
        // select b, w from BoardEntity b left join b.writer w on b.writer = w;

        // select b, w, count(r) from BoardEntity b left join b.writer w left join ReplyEntity r on r.board = b;
        //JPQLQuery<Tuple> tuple = jpqlQeury.select(boardEntity, memberEntity, replyEntity.count());
        JPQLQuery<Tuple> tuple = jpqlQeury.select(boardEntity, memberEntity);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression= boardEntity.bno.gt(0L); // sequence number > 0L 모두 만족하므로 모두임

        booleanBuilder.and(expression);

        if(type != null) {
            String[] typeArr = type.split("");
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for(String t : typeArr) {
                switch (t) {
                    case "t":
                        conditionBuilder.or(boardEntity.title.contains(keyword)); // 게시물 제목
                        break;
                    case "w":
                        conditionBuilder.or(memberEntity.email.contains(keyword));  // 게시물 저자
                        break;
                    case "c":
                        conditionBuilder.or(boardEntity.content.contains(keyword));  // 게시물 내용
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }
        tuple.where(booleanBuilder);

        // order by
        Sort sort = pageable.getSort();
        // tuple.orderBy(board.bno.desc());
        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC: Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(BoardEntity.class, "boardEntity");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        tuple.groupBy(boardEntity);

        // page 처리
        tuple.offset(pageable.getOffset());  // 시작 레코드 vs 현재 페이지를 사용하지는 않음
        tuple.limit(pageable.getPageSize()); // 레코드 수

        List<Tuple> result = tuple.fetch(); // 데이터 소스로 부터 정보를 가져옴

        long count = tuple.fetchCount(); // 가져온(fetch) 갯수를 확인
        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
    }
}
