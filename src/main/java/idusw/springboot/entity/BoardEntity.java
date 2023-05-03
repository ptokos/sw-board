package idusw.springboot.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "sw_board")

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer")
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sw_board_seq_gen")
    @SequenceGenerator(sequenceName = "sw_board_seq", name = "sw_board_seq_gen", allocationSize = 1)
    private Long bno; // 유일키

    private String title; // 제목
    private String content; // 내용
    private Long views; // 조회수
    private String block; // 차단여부

    @ManyToOne
    private MemberEntity writer;  //연관 관계 지정 : 작성자 1명 - 게시물 다수
}
