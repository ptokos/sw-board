package idusw.springboot.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import idusw.springboot.domain.Member;
import idusw.springboot.domain.PageRequestDTO;
import idusw.springboot.domain.PageResultDTO;
import idusw.springboot.entity.MemberEntity;
import idusw.springboot.entity.QMemberEntity;
import idusw.springboot.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


@Service
public class MemberServiceImpl implements MemberService {
    // DI - IoC (Inversion of Control : 제어의 역전) 방법 중 하나, DI, DL ...
    //
    MemberRepository memberRepository;
    public MemberServiceImpl(MemberRepository memberRepository) { // Spring Framework이 주입(하도록 요청함)
        this.memberRepository = memberRepository;
    }

    @Override
    public int create(Member m) {
        MemberEntity entity = MemberEntity.builder()
                .seq(m.getSeq())
                .email(m.getEmail())
                .name(m.getName())
                .pw(m.getPw())
                .build();
        // memberRepository에서  findByEmail을 해서 결과가 있으면, 중복 => 실패
        // 결과가 없으면 성공
//        if (memberRepository.findByEmail(entity.getEmail()) != null) {
//            System.out.println("중복 이메일로 저장 실패");
//            return 0;
//        }
        if(memberRepository.save(entity) != null) // 저장 성공
            return 1;
        else
            return 0;
    }

    @Override
    public Member read(Member m) {
        MemberEntity e = memberRepository.getById(m.getSeq()); // JpaRepository 구현체의 메소드
        Member result = new Member(); // DTO (Data Transfer Object) : Controller - Service or Controller - View
        System.out.println(e);
        result.setSeq(e.getSeq());
        result.setEmail(e.getEmail());
        result.setName(e.getName());
        return result;
    }

    @Override
    public List<Member> readList() {
        List<MemberEntity> entities = new ArrayList<>();
        List<Member> members = null;
        if((entities = memberRepository.findAll()) != null) {
            members = new ArrayList<>();
            for(MemberEntity e : entities) {
                Member m = Member.builder()
                        .seq(e.getSeq())
                        .email(e.getEmail())
                        .name(e.getName())
                        .pw(e.getPw())
                        .phone(e.getPhone())
                        .address(e.getAddress())
                        .regDate(e.getRegDate())
                        .modDate(e.getModDate())
                        .build();
                members.add(m);
            }
        }
        return members;
    }

    @Override
    public int update(Member m) {
        MemberEntity entity = MemberEntity.builder()
                .seq(m.getSeq())
                .email(m.getEmail())
                .name(m.getName())
                .pw(m.getPw())
                .phone(m.getPhone())
                .address(m.getAddress())
                .build();
        if(memberRepository.save(entity) != null) // 저장 성공
            return 1;
        else
            return 0;
    }

    @Override
    public int delete(Member m) {
        MemberEntity entity = MemberEntity.builder()
                .seq(m.getSeq())
                .build();
        memberRepository.deleteById(entity.getSeq());
        return 1;
    }

    @Override
    public Member login(Member m) {
        MemberEntity e = memberRepository.getByEmailPw(m.getEmail(), m.getPw()); // JpaRepository 구현체의 메소드
        System.out.println("login : " + e);
        Member result = null; // DTO (Data Transfer Object) : Controller - Service or Controller - View
        if(e != null) {
            result = new Member();
            result.setSeq(e.getSeq());
            result.setEmail(e.getEmail());
            result.setName(e.getName());
            result.setPhone(e.getPhone());
            result.setAddress(e.getAddress());
        }
        return result;
    }

    @Override
    public int checkEmail(Member m) {
        List<MemberEntity> memberEntityList = memberRepository.getMemberEntitiesByEmail(m.getEmail());
        if(memberEntityList.size() > 0)
            return 1; // email 중복
        else
            return 0; // 사용가능
    }

    @Override
    public PageResultDTO<Member, MemberEntity> getList(PageRequestDTO requestDTO) {
        Sort sort = Sort.by("seq").descending(); //  .ascending();

        Pageable pageable = requestDTO.getPageable(sort);

        BooleanBuilder booleanBuilder = findByCondition(requestDTO);
        Page<MemberEntity> result = memberRepository.findAll(booleanBuilder, pageable);

        Function<MemberEntity, Member> fn = (entity -> entityToDto(entity));

        PageResultDTO PageResultDTO = new PageResultDTO<>(result, fn, requestDTO.getPerPagination());

        return PageResultDTO;
    }

    private BooleanBuilder findByCondition(PageRequestDTO pageRequestDTO) {
        String type = pageRequestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMemberEntity qMemberEntity = QMemberEntity.memberEntity;
        BooleanExpression expression = qMemberEntity.seq.gt(0L);
        booleanBuilder.and(expression);
        if(type == null || type.trim().length() == 0){
            return booleanBuilder;
        }

        String keyword = pageRequestDTO.getKeyword();

        BooleanBuilder conditionBuilder = new BooleanBuilder();
        //select * from member where
        // seq > 0
        // email=keyword or name=keyword
        // seq > 0 and email=keyword or name=keyword
        //select * from member where seq > 0 and email=keyword or name = keyword
        if(type.contains("e")) { // email 검색
            conditionBuilder.or(qMemberEntity.email.contains(keyword));  //or 메소드를 불러오면 or가 조건에 붙는다 / 함수 호출관계로 접근 JPA
        }
        if(type.contains("n")) { // name 검색
            conditionBuilder.or(qMemberEntity.name.contains(keyword));
        }
        booleanBuilder.and(conditionBuilder);
        return booleanBuilder;
    }

}
