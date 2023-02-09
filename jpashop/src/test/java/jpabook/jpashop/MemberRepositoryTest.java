package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class MemberRepositoryTest {
/*
    @Autowired MemberRepository memberRepository;

    //custom에 livetemplate을 통해서 단축키를 만들 수 있다
    @Test
    @Transactional //트랜잭션을 생성주어야한다 테스트케이스에 있으면 테스트가 끝난다음 롤백해버린다
    @Rollback(false) //테스트가 끝난후 롤백을 하지 않음
    public void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setUsername("memberA");
        ///when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }*/


}