package idusw.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data // == @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
public class PageRequestDTO {
    private int page; // 요청하는 페이지
    private int perPage; // 페이지당 게시물 수
    private int perPagination; // 한 화면에 나타나는 페이지 수에 대한 갯수

    private String type; // 검색 유형
    private String keyword; // 검색어

    public PageRequestDTO() {
        this.page = 1;
        this.perPage = 8;
        this.perPagination = 5;
    }
    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page - 1, perPage, sort);
    }
}