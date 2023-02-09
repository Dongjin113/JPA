package jpabook.jpashop.Service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional//JPA의 모든 데이터 변경이나 로직들은 트랜잭션 안에서 실행되야함으로 Transcational이 있어야한다
/*
@AllArgsConstructor //Lombok 필드의 모든걸가지고 똑같은 생성자를 만들어준다
 @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
*//*
@RequiredArgsConstructor
    private final MemberRepository memberRepository;
    final에 있는 필드만 가지고 생성자를 만들어준다
    */
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    //setter injection

/*

    //생성자 인젝션 요즘추천방법
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
*/

    //회원가입
    @Transactional
    public Long join(Member member){
        
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

    //회원전체조회
    @Transactional(readOnly = true) //readOnly 읽기전용 트랜잭션
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //한건조회
    @Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
