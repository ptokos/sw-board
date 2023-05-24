package idusw.springboot.domain;

import java.time.LocalDateTime;

public class Board {
    private Long bno; // 유일키
    private String title; // 제목
    private String content; // 내용

    private Long writerSeq;
    private String writerEmail;
    private String writerName;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

}
