package idusw.springboot.controller;

import idusw.springboot.domain.Member;
import idusw.springboot.domain.PageRequestDTO;
import idusw.springboot.domain.PageResultDTO;
import idusw.springboot.entity.MemberEntity;
import idusw.springboot.repository.MemberRepository;
import idusw.springboot.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberControllerTests {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional  // could not initialize proxy - no Session : Lazy fetch 로 인한 오류
    // Lazy fetch - 스프링에서는 fetch를 최대한 늦게함
    // 쿼리를 연속적으로 할 때 순서가 꼬이면 오류 발생
    // -> 트랜잭션(함께 처리해야할 단위), ex) 은행 -  개인계좌에서 출금하면 은행에서도 동시에 출금수행

    void readMember() { // seq를 사용해야 함
        Member member = new Member();
        member.setSeq(51L);
        Member result = null;
        if((result = memberService.read(member)) != null )
            System.out.println("조회 성공! " + result.getEmail() + ":::" + result.getName());
        else
            System.out.println("조회 실패!");
    }

    @Test
    void readMemberList() {
        List<Member> resultList = null;
        if((resultList = memberService.readList()) != null) {
            for(Member m : resultList)
                System.out.format("%-10s | %-10s | %10s\n", m.getName(), m.getEmail(), m.getRegDate());
        }
        else
            System.out.println("목록 없음");
    }

    @Test
    void initializeMember() {
        // Integer 데이터 흐름, Lambda 식 - 함수형 언어의 특징을 활용
        IntStream.rangeClosed(1, 100).forEach(i -> {
            MemberEntity member = MemberEntity.builder()
                    .seq(Long.valueOf(i))
                    .email("e" + i + "@induk.ac.kr") // 17039
                    .pw("pw" + i)
                    .name("name" + i)
                    .build();
            memberRepository.save(member);
        });
    }

    @Test
    void createMember() {
        Member member = Member.builder()
                .email("13@13.com")
                .name("13")
                .pw("13")
                .build();
        if(memberService.create(member) > 0 ) // 정상적으로 레코드의 변화가 발생하는 경우 영향받는 레코드 수를 반환
            System.out.println("등록 성공");
        else
            System.out.println("등록 실패");
    }

    @Test
    public void testPageList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(5)  // 현재 페이지
                .perPage(5)  //레코드 수
                .perPagination(5)  // 페이지 번호 표시 갯수
                .build();
        PageResultDTO<Member, MemberEntity> resultDTO = memberService.getList(pageRequestDTO);
        // print records in page
        for(Member member : resultDTO.getDtoList())
            System.out.println(member);

        //boolean 은 lombok으로 generation 할 때 isPrev()를 생성함, setter 는 setPrev()
        //int totalPage인 경우 getter는  getTotalPage(), setter setTotalPage()
        // @Data == @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode.

        System.out.println("Prev : " + resultDTO.isPrev());
        System.out.println("Next : " + resultDTO.isNext());
        System.out.println("Total Page : " + resultDTO.getTotalPage());
       // resultDTO.getPageList().forEach(i -> System.out.println(i));
        for(Integer i : resultDTO.getPageList())
            System.out.format("%3d", i);
    }
}
