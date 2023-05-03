package idusw.springboot.domain;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data //5가지 애노테이션 묶어놓은 것
public class PageResultDTO<DTO, EN> {
    private List<DTO> dtoList;

    private int totalPage;
    private int curPage; // 현재 페이지 번호
    private int size;

    private int start, end; // 시작 페이지 번호, 끝 페이지 번호
    private boolean prev, next;

    private List<Integer> pageList; // 페이지 번호 목록

    public  PageResultDTO(Page<EN> result, Function<EN,DTO>fn){
        dtoList = result.stream().map(fn).collect(Collectors.toList());
    }



}
