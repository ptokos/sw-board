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
public class PageRequestDTO {
    private int page;
    private int size;

    public PageRequestDTO() {
        this.page = 1;
        this.size = 5;
    }

    public Pageable getPageable(Sort sort) {
        return  PageRequest.of(page -1, size, sort); // 0부터 처리하는 구조 따라서 0이 1번째 페이지이다

    }
}
