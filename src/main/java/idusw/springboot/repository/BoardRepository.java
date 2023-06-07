package idusw.springboot.repository;

import idusw.springboot.entity.BoardEntity;
import idusw.springboot.repository.search.SearchBoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long>, SearchBoardRepository {
    @Query(value = "select b, w, count(r) " +
            "from BoardEntity b left join b.writer w " +
            "left join ReplyEntity r on r.board = b " +
            "where b.bno = :bno group by b, w")
    Object getBoardByBno(@Param("bno") Long bno);
}
