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
import idusw.springboot.entity.QReplyEntity;
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

        // Querydsl에서 사용할 Entity와 Entity의 속성을 나타내는 Q 클래스 생성
        QBoardEntity boardEntity = QBoardEntity.boardEntity;
        QReplyEntity replyEntity = QReplyEntity.replyEntity;
        QMemberEntity memberEntity = QMemberEntity.memberEntity;

        // JPQLQuery 객체 생성하여 쿼리 작성을 위한 시작점 설정
        JPQLQuery<BoardEntity> jpqlQuery = from(boardEntity);

        // 게시물 작성자와의 조인 설정
        jpqlQuery.leftJoin(memberEntity).on(boardEntity.writer.eq(memberEntity));

        // 댓글과의 조인 설정
        jpqlQuery.leftJoin(replyEntity).on(replyEntity.board.eq(boardEntity));

        // select b, w from BoardEntity b left join b.writer w on b.writer = w;
        // 게시물과 작성자를 조인하여 조회하는 쿼리 작성

        // select b, w, count(r) from BoardEntity b left join b.writer w left join ReplyEntity r on r.board = b;
        // 게시물과 작성자, 댓글 수를 조회하는 쿼리 작성
        JPQLQuery<Tuple> tuple = jpqlQuery.select(boardEntity, memberEntity, replyEntity.count());

        // BooleanBuilder를 사용하여 조건절 설정
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = boardEntity.bno.gt(0L); // 게시물의 일련번호가 0보다 크다는 조건 추가

        booleanBuilder.and(expression);

        if (type != null) {
            // 검색 타입(type)에 따라 조건을 설정하기 위한 분기문
            String[] typeArr = type.split("");
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t : typeArr) {
                switch (t) {
                    case "t":
                        conditionBuilder.or(boardEntity.title.contains(keyword)); // 게시물 제목에 키워드가 포함된다는 조건 추가
                        break;
                    case "w":
                        conditionBuilder.or(memberEntity.email.contains(keyword));  // 게시물 작성자의 이메일에 키워드가 포함된다는 조건 추가
                        break;
                    case "c":
                        conditionBuilder.or(boardEntity.content.contains(keyword));  // 게시물 내용에 키워드가 포함된다는 조건 추가
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }
        tuple.where(booleanBuilder);

        // 정렬 설정
        Sort sort = pageable.getSort();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(BoardEntity.class, "boardEntity");
            // 정렬 기준(prop)에 따라 정렬 방식(direction)을 설정하여 OrderSpecifier 생성
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        // 게시물과 작성자를 그룹으로 묶기 위해 groupBy 설정
        tuple.groupBy(boardEntity, memberEntity);

        // 페이지네이션 처리
        tuple.offset(pageable.getOffset());  // 시작 레코드 인덱스
        tuple.limit(pageable.getPageSize()); // 페이지당 레코드 수

        // 결과 조회 및 페이징 처리를 위한 PageImpl 객체 생성
        List<Tuple> result = tuple.fetch(); // 실제 데이터 가져오기
        long count = tuple.fetchCount(); // 전체 데이터 수 가져오기

        // Tuple 객체를 Object 배열로 변환하고, PageImpl 객체 생성하여 반환
        return new PageImpl<Object[]>(
                result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable,
                count
        );
    }
}
