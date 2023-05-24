package idusw.springboot.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "ab_board")


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString//(exclude = "writer")
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ab_board_seq_gen")
    @SequenceGenerator(sequenceName = "ab_board_seq", name = "ab_board_seq_gen", initialValue = 1, allocationSize = 1)
    private Long bno; // 유일키
    @Column(length = 50, nullable = false)
    private String title; // 제목
    @Column(length = 1000, nullable = false)
    private String content; // 내용

    @ManyToOne
    //@JoinColumn(name = "seq")
    private MemberEntity writer; // BoardEntity : MemberEntity = N : 1

/*
    private String title; // 제목
    private String content; // 내용
    private Long views; // 조회수
    private String block; // 차단여부



    @ManyToOne
    private MemberEntity writer;  //연관 관계 지정 : 작성자 1명 - 게시물 다수
    */

}
