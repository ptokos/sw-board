package idusw.springboot.repository;

import idusw.springboot.entity.BoardEntity;
import idusw.springboot.repository.search.SearchBoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long>, SearchBoardRepository {

}
