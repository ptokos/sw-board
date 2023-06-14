package idusw.springboot.repository;

import idusw.springboot.entity.BoardEntity;
import idusw.springboot.repository.search.SearchBoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query("update BoardEntity b set b.likeCount = :likeCount where b.bno = :bno")
    void setLikeCount(@Param("bno") Long bno, @Param("likeCount") Integer likeCount);

    @Query("select b.likeCount from BoardEntity b where b.bno = :bno")
    Integer getLikeCount(@Param("bno") Long bno);
}
