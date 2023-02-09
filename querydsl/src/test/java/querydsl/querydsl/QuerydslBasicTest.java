package querydsl.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import querydsl.querydsl.dto.MemberDto;
import querydsl.querydsl.dto.QMemberDto;
import querydsl.querydsl.dto.UserDto;
import querydsl.querydsl.entity.Member;
import querydsl.querydsl.entity.QMember;
import querydsl.querydsl.entity.QTeam;
import querydsl.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.sql.SQLOutput;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static querydsl.querydsl.entity.QMember.*;
import static querydsl.querydsl.entity.QTeam.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before(){
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",20,teamA);

        Member member3 = new Member("member3",30,teamB);
        Member member4 = new Member("member4",40,teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL(){
        //member1을 찾아라
        String qlString = "select m from Member m where m.username = :username";
        Member findByJPQL = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findByJPQL.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl(){
//        QMember m = new QMember("m");
//        QMember m = QMember.member;

        Member findMember = queryFactory
//                QMember.member 에서 static import를 사용해서  member로 깔끔하게 사용할 수 있다.
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void search(){
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1").and(member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");

        /*
        * member.username.eq("member1") // username = 'member1'
        member.username.ne("member1") //username != 'member1'
        member.username.eq("member1").not() // username != 'member1'
        member.username.isNotNull() //이름이 is not null
        member.age.in(10, 20) // age in (10,20)
        member.age.notIn(10, 20) // age not in (10, 20)
        member.age.between(10,30) //between 10, 30
        member.age.goe(30) // age >= 30
        member.age.gt(30) // age > 30
        member.age.loe(30) // age <= 30
        member.age.lt(30) // age < 30
        member.username.like("member%") //like 검색
        member.username.contains("member") // like ‘%member%’ 검색
        member.username.startsWith("member") //like ‘member%’ 검색
        * */
    }

    @Test
    public void resultFetch(){
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        Member fetchOne = queryFactory
                .selectFrom(member)
                .where(member.id.eq(1L))
                .fetchOne();

        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();
    }
/*
* 회원 정렬 순서
* 1. 회원 나이 내림차순 (desc)
* 2. 회원 이름 올린차순(asc)
* 단 2에서 회원 이름이 없으면 마지막에 출력
*/
    @Test
    public void sort(){

        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    public void paging(){
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        Long count = queryFactory
                .select(member.count())
                .from(member)
                .fetchOne();

        assertThat(count).isEqualTo(4);
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void aggregation(){
        List<Tuple> result = queryFactory
                .select(member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();
        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }

//  팀의 이름과 각 팀의 평균 연령을 구해라
    @Test
    public void group(){
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .orderBy(team.name.asc())
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);

    }

    @Test
    public void join(){
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        for (Member member1 : result) {
            System.out.println("member1 = "+ member1);
        }

        assertThat(result)
                .extracting("username")
                .containsExactly("member1","member2");

    }

    /*세타 조인 (연관관계가 없는 필드로 조인)bnmvmnbvnm
    * 회원의 이름이 팀 이름과 같은 회원 조회*/
    @Test
    public void theta_Join(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA","teamB");
    }

    /*
    * 예) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
    * JPAL: select m, t from Member m left join m.team t on t.name = 'teamA'*/
    @Test
    public void join_one_filtering(){
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
//               멤버는 다가져오지만 team은 여러팀중에서 teamA인것만 가져오는 것
                .leftJoin(member.team, team).on(team.name.eq("teamA"))
                .fetch();

        /* 결과값
        tuple = [Member(id=3, username=member1, age=10), Team(id=1, name=teamA)]
        tuple = [Member(id=4, username=member2, age=20), Team(id=1, name=teamA)]
        tuple = [Member(id=5, username=member3, age=30), null]
        tuple = [Member(id=6, username=member4, age=40), null]*/
        for (Tuple tuple : result) {
            System.out.println("tuple = "+ tuple);
        }
        
//       Inner join의 경우 on절이아닌 where절로 사용해도 같은값을 출력할 수 있다

        List<Tuple> innerjoin = queryFactory
                .select(member, team)
                .from(member)
//               멤버는 다가져오지만 team은 여러팀중에서 teamA인것만 가져오는 것
                .join(member.team, team).where(team.name.eq("teamA"))
                .fetch();
    }

    @Test
    public void join_on_no_relation(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void fetchJoinNo(){
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member)
                .where(member.username.eq("member1"))
                .fetchOne();

//로딩됐는지 초기화가안됐는지 가르쳐주는애 당연히 false가 나와야함 Lazy설정이 돼있기 때문
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("패치 조인 미적용").isFalse();
    }

    @Test
    public void fetchJoinUse(){
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team , team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

//로딩됐는지 초기화가안됐는지 가르쳐주는애 당연히 false가 나와야함 Lazy설정이 돼있기 때문
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("패치 조인 미적용").isTrue();
    }

    /*
    * 서브쿼리
    * */
    @Test
    public void subQuery(){
        QMember memberSub = new QMember("memberSub");

//         나이가 가장많은 회원
        List<Member> result1 = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

//        나이가 평균이상인 회원
        List<Member> result2 = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();
        
//       나이가 10살초과 in절
        List<Member> result3 = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();

        assertThat(result1).extracting("age")
                .containsExactly(40);
        assertThat(result2).extracting("age")
                .containsExactly(30,40);
        assertThat(result3).extracting("age")
                .containsExactly(20,30,40);
    }

    @Test
    public  void selectSubQuery(){
//       JPQL 서브쿼리의 한계점으로 from 절의 서브쿼리는 지원하지 않는다
        QMember memberSub = new QMember("memberSub");

        List<Tuple> result = queryFactory
                .select(member.username,
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void basicCase(){
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void complexCas(){
        List<String> result = queryFactory.select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20")
                        .when(member.age.between(21, 30)).then("21~30")
                        .otherwise("기타"))
                .from(member)
                .fetch();
        for (String s : result) {
            System.out.println("s = "+ s);
        }
    }

    /*상수*/
    @Test
    public void constant(){
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " +tuple);
        }
    }

    @Test
    public void concat(){

        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void SimpleProjection(){
        queryFactory
                .select(member.username)
                .from(member)
                .fetch();
    }


    @Test
    public void findDtoByJPQL(){
        List<MemberDto> result = em.createQuery("select new querydsl.querydsl.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                .getResultList();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void findDtoBySetter(){
        List<MemberDto> result = queryFactory
//               setter를 통해서 값이 들어간다
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = "+memberDto);
        }
    }

    @Test
    public void findDtoByField(){
        List<MemberDto> result = queryFactory
//               getter setter가 없어도 filed에 값이 그냥 꽃힌다
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = "+memberDto);
        }
    }

    @Test
    public void findUserDtoByField(){
        List<UserDto> result = queryFactory
//               getter setter가 없어도 filed에 값이 그냥 꽃힌다
//               field의 값이 다를때에는 as를 통해서 맵핑할 수 있다
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        member.age))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = "+userDto);
        }
    }

    @Test
    public void findUserDto(){
        QMember memberSub = new QMember("memberSub");
        List<UserDto> result = queryFactory
//               getter setter가 없어도 filed에 값이 그냥 꽃힌다
//               field의 값이 다를때에는 as를 통해서 맵핑할 수 있다
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),

                        ExpressionUtils.as(JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub),"age")
                ))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = "+userDto);
        }
    }

    @Test
    public void findDtoByConstructor(){
        List<MemberDto> result = queryFactory
//              생성자를 통해 값을생성
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = "+memberDto);
        }
    }

    @Test
    public void findDtoByQueryProjection(){
//        생성자로 사용한다면 런타임오류가 발생 QueryProjection은 다른 데이터가 추가로 들어오게 된다면 컴파일 오류로 잡아준다
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void dynamicQuery_BooleanBuilder(){
        String usernameParam = "member1";
        Integer ageParam = null;

        List<Member> result = searchMember1(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember1(String usernameCond, Integer ageCond) {

        BooleanBuilder builder = new BooleanBuilder();
//        만약 usernameCond가 필수다 하면 초기값을 넣을 수 있다
//        BooleanBuilder builder = new BooleanBuilder(member.username.eq(usernameCond));

        if(usernameCond != null){
            builder.and(member.username.eq(usernameCond));
        }

        if(ageCond != null){
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    @Test
    public void dynamicQuery_WhereParam(){
        String usernameParam = "member1";
        Integer ageParam = null;

//      parameter 값에 null이 들어오면 아예 무시를 해버린다 고로 username 에대한 where만 실행이 된다
        List<Member> result = searchMember2(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);


    }


    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
//                .where(usernameEq(usernameCond),ageEq(ageCond))
                .where(usernameEq(usernameCond), ageEq(ageCond))
                .fetch();
    }

    private BooleanExpression ageEq(Integer ageCond) {
        return ageCond != null ? member.age.eq(ageCond) : null;
    }

    private BooleanExpression usernameEq(String usernameCond) {
        return usernameCond != null ?  member.username.eq(usernameCond) : null ;
    }

    private BooleanExpression allEq(String usernameCond, Integer ageCond){
       return usernameEq(usernameCond).and(ageEq(ageCond));
    }

    @Test
    public void bulkUpdate(){
        //member1 = 10 -> 비회원
        //member2 = 20 -> 비회원
        //member3 = 30 -> 유지
        //member4 = 40 -> 유지
        long count = queryFactory
                .update(member)
                .set(member.username,"비회원")
                .where(member.age.lt(28))
                .execute();
    }

    @Test
    public void bulkAdd(){
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
//                .set(member.age, member.age.multiply(2)) //곱하기
                .execute();
    }

    @Test
    public void bulkDelete(){
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();
    }

    @Test
    public void sqlFunction(){
        List<String> result = queryFactory
                .select(
                        Expressions.stringTemplate(
                                "function('replace',{0},{1},{2})",
//                                member라는 이름을 M으로 바꿔서 조회한다
                                member.username, "member", "M"))
                .from(member)
                .fetch();
        for (String s : result) {
            System.out.println("s = "+ s);
        }
    }

    @Test
    public void sqlFunction2(){
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
//                .where(member.username.eq(
//                        Expressions.stringTemplate("function('lower', {0})", member.username)))
                .where(member.username.eq(member.username.lower()))
                .fetch();

        for (String s : result) {
            System.out.println("s1 = "+s);
        }


    }
}
