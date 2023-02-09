package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        findMember1.setUsername("member11123");

        //리스트 조회 검증
        List<Member> all =memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

//        삭제검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        List<Member> all1 = memberRepository.findAll();
        assertThat(all1.size()).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);


    }

    @Test
    public void findByUsernameAndAgeGreaterThen2(){
        memberRepository.findHelloBy();
    }

    @Test
    public void testQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findusernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = "+ s);
        }

    }

    @Test
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10, team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();

        for (MemberDto dto : memberDto) {
            System.out.println("디티오 = "+ dto);
        }
    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA","BBB"));
        for (Member member : result) {
            System.out.println("member = "+ member);
        }

    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

//        없으면 빈콜렉션을 반환
        List<Member> findMember = memberRepository.findListByUsername("AAb");
        System.out.println("findMember = " +findMember);

        Optional<Member> aaa = memberRepository.findOptionalByUsername("AAn");
        System.out.println("aaa = " + aaa);

//        없으면 null반환
        Member aaa1 = memberRepository.findMemberByUsername("AAA");
        System.out.println("aaa1 = "+ aaa1);
    }

    @Test
    public void paging(){
        //given
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));
        memberRepository.save(new Member("member6",10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

//        반환타입이 Page라면 Totalcount까지 알아서 해준다
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = "+member);
        }

        System.out.println("totalElements = "+ totalElements);

        assertThat(content.size()).isEqualTo(3); //조회된 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(6); //전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); //전체 페이지 번호
        assertThat(page.isFirst()).isTrue(); //첫번째 항목인가?
        assertThat(page.hasNext()).isTrue(); //다음 항목이있느냐?
    }

    @Test
    public void bulkUpdate(){

        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",19));
        memberRepository.save(new Member("member3",20));
        memberRepository.save(new Member("member4",21));
        memberRepository.save(new Member("member5",40));

        int resultCount = memberRepository.bulkAgePlus(20);
        em.flush();
        em.clear();

        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);
//      벌크연산은 영속성 컨텍스트를 무시하고 db에 바로 때려박기때문에
//      db는 반영이 됐지만 영속성 컨텍스트에서는 반영이되지않아 40살로 나온다
        System.out.println("member5" + member5);

        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamA");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member1",10,teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

//        List<Member> members = memberRepository.findMemberFetchJoin();
            List<Member> members =memberRepository.findEntityGraphByUsername("member1");
        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.teamClass = "+ member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint(){
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

//        Member findMember = memberRepository.findById(member1.getId()).get();
//        findMember.setUsername("member2");

//        Member findMember = memberRepository.findReadOnlyByUsername("member1");
////        readOnly true 설정이 되있으면 최적화를 위해 변경이 안된다는 가정하에 다무시한다
//        findMember.setUsername("member2");

        List<Member> findMember = memberRepository.findLockByUsername("member1");
//        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void callCustom(){
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    public void queryByExample(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1",0,teamA);
        Member m2 = new Member("m2",0,teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Member member = new Member("m1");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");

        Example<Member> example = Example.of(member, matcher);

        List<Member> result = memberRepository.findAll(example);
        assertThat(result.get(0).getUsername()).isEqualTo("m1");
    }

    
//    interface기반 프로젝션
    @Test
    public void projections() {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        List<UsernameOnly> result = memberRepository.findProjectionsByUsername("m1");

        for (UsernameOnly usernameOnly : result) {
//            getUsername에 Value target.을 설정하면 값을 찾아서 설정한 값을 출력한다
            System.out.println("usernameOnly = "+ usernameOnly.getUsername());
        }
    }

    @Test
    public void projectionsDto() {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

//       UsernameOnlyDto parameter name으로 맵핑해서 가져온다
        List<NestedClosedProjections> result = memberRepository.findProjectionsClassByUsername("m1", NestedClosedProjections.class);

        for (NestedClosedProjections usernameOnly : result) {
//            getUsername에 Value target.을 설정하면 값을 찾아서 설정한 값을 출력한다
            System.out.println("usernameOnly = "+ usernameOnly.getUsername());
            System.out.println("usernameOnly = "+ usernameOnly.getTeam().getName());

        }
    }

    @Test
    public void nativeQuery(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

//        Member result = memberRepository.findByNativeQuery("m1");
//        System.out.println("result = "+ result);

        Page<MemberProjection> result1 = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        List<MemberProjection> content = result1.getContent();

        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjection = "+memberProjection.getTeamName());
            System.out.println("memberProjection = "+memberProjection.getUsername());

        }

    }



}