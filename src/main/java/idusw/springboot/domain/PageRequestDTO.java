package idusw.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
/**
 * Page 요청 객체 <br>
 * Page, Pageable, PageRequest 객체가 연관 있음
 */
public class PageRequestDTO {
    private int page; // 요청하는 페이지
    private int perPage; // 페이지당 게시물 수
    private int perPagination;

    private String type; // 검색유형
    private String keyword; // 한 화면에 나타나는 페이지 수에 대한 갯수

    public PageRequestDTO() {
        this.page = 1;
        this.perPage = 10;
    }

    public Pageable getPageable(Sort sort) {
        return  PageRequest.of(page -1, perPage, sort); // 0부터 처리하는 구조 따라서 0이 1번째 페이지이다

    }
}
