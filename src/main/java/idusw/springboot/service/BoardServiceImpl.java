package idusw.springboot.service;


import idusw.springboot.domain.Board;
import idusw.springboot.domain.PageRequestDTO;
import idusw.springboot.domain.PageResultDTO;
import idusw.springboot.entity.BoardEntity;
import idusw.springboot.entity.MemberEntity;
import idusw.springboot.repository.BoardRepository;
import idusw.springboot.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;


    @Override
    public int registerBoard(Board board) {
        BoardEntity entity = dtoToEntity(board);

        if(boardRepository.save(entity) != null) // 저장 성공
            return 1;
        else
            return 0;
    }

    @Override
    public Board findBoardById(Board board) {
        Object[] entities = (Object[]) boardRepository.getBoardByBno(board.getBno());
        return entityToDto((BoardEntity) entities[0], (MemberEntity) entities[1], (Long) entities[2]);
    }

    @Override
    public PageResultDTO<Board, Object[]> findBoardAll(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("bno").descending());
        Page<Object[]> result = boardRepository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno")));
        Function<Object[], Board> fn = (entity -> entityToDto((BoardEntity) entity[0],
                (MemberEntity) entity[1], (Long) entity[2]));
        return new PageResultDTO<>(result, fn, 5);
    }

    @Transactional
    @Override
    public int updateBoard(Board board) {
        return 0;
    }

    @Transactional
    @Override
    public int deleteBoard(Board board) {
      replyRepository.deleteByBno(board.getBno()); // 댓글 삭제
        boardRepository.deleteById(board.getBno()); // 게시물 삭제

        return 0;
    }

    @Override
    @Transactional
    public int likeBoard(Board board) {
        Long bno = board.getBno();
        BoardEntity boardEntity = boardRepository.findById(bno).orElse(null);
        if (boardEntity != null) {
            Integer likeCount = boardEntity.getLikeCount() != null ? boardEntity.getLikeCount() : 0;
            boardRepository.setLikeCount(bno, likeCount + 1);

            return 1; // 좋아요 수 업데이트 성공
        }

        return 0; // 게시물을 찾지 못함
    }



}
