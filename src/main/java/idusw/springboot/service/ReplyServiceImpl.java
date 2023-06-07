package idusw.springboot.service;

import idusw.springboot.domain.Board;
import idusw.springboot.domain.Reply;
import idusw.springboot.entity.BoardEntity;
import idusw.springboot.entity.ReplyEntity;
import idusw.springboot.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    /*
    public ReplyServiceImpl(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }
    */
    @Override
    public Long register(Reply reply) {
        ReplyEntity entity = dtoToEntity(reply);
        return Long.valueOf((replyRepository.save(entity) != null)? 1 :0);
    }

    @Override
    public List<Reply> getList(Long bno) {
        BoardEntity boardEntity = BoardEntity.builder().bno(bno).build();
        List<ReplyEntity> result = replyRepository.getRepliesByBoardOrderByRno(boardEntity);
        return result.stream().map(entity -> entityToDto(entity)).collect(Collectors.toList());
    }

    @Override
    public void modify(Reply reply) {
        replyRepository.save(dtoToEntity(reply));
    }

    @Override
    public void remove(Reply reply) {
        replyRepository.deleteById(reply.getRno());
    }
}
