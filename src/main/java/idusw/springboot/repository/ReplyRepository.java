package idusw.springboot.repository;

import idusw.springboot.entity.BoardEntity;
import idusw.springboot.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
    @Modifying
    @Query("delete from ReplyEntity r where r.board.bno = :bno")
    void deleteByBno(Long bno);

    List<ReplyEntity> getRepliesByBoardOrderByRno(BoardEntity board);
    //List<ReplyEntity> findReplyEntitiesByBoardByRno(BoardEntity board);
}