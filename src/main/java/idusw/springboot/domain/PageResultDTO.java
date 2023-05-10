package idusw.springboot.domain;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data //5가지 애노테이션 묶어놓은 것
public class PageResultDTO<DTO, EN> {
    private List<DTO> dtoList;

    private int totalPage; // 총 페이지 번호 = (총 레코드수 / 한 페이지 게시물 갯수 = 값을 올림해줌), 나머지가 있다면 + 1
    private int curPage; // 현재 페이지 번호
    private int size; // 한 페이지 게시물 갯수

    private int start, end; // 시작 페이지 번호, 끝 페이지 번호
    private boolean prev, next; // 버튼 표시

    private List<Integer> pageList; // 페이지 번호 목록

    public  PageResultDTO(Page<EN> result, Function<EN,DTO>fn){
        dtoList = result.stream().map(fn).collect(Collectors.toList()); // get records
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        this.curPage = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();
        int tempEnd = (int)(Math.ceil(curPage / 3.0)) * 3;
        start = tempEnd - 3 + 1;
        prev = start > 1; // 1보다 크면 true, 작으면 false
        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList()); //get pageNumber List

    }


}