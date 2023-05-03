package idusw.springboot.domain;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequestDTO {
    private int page;
    private int size;

    public PageRequestDTO() {
        this.page = 1;
        this.size = 5;
    }
    public Pageable getPageable(Sort sort) {
        return  PageRequest.of(page -1, size, sort);

    }
}
