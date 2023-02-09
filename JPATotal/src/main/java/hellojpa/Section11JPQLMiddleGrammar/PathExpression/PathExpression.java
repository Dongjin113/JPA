package hellojpa.Section11JPQLMiddleGrammar.PathExpression;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class PathExpression {

    /* 경로 표현식
    - .(점)을 찍어 객체 그래프를 탐색하는 것
    select m.username -> 상태 필드
        from Member m
            join m.team t -> 단일 값 연관 필드
            join m.order o -> 컬렉션 값 연관 필드
        where t.name = '팀 A'

   *  경로 표현식 용어 정리
        - 상태 필드(state field): 단순히 값을 저장하기 위한 필드 (ex: m.username)
        - 연관 필드(association field): 연관관계를 위한 필드
            = 단일 값 연관 필드
            @ManyToOne, @OneToOne, 대상이 엔티티(ex: m.team)
            = 컬렉션 값 연관 필드:
            @OneToMany, @ManyToMany, 대상이 컬렉션(ex: m.orders)

    * 경로 표현식 특징
        - 상태 필드(state field): 경로 탐색의 끝, 탐색X
        - 단일 값 연관 경로: 묵시적 내부 조인(inner join) 발생, 탐색O
        - 컬렉션 값 연관 경로 : 묵시적 내부 조인 발생, 탐색X
            = FROM 절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색 가능
            
    !!!실무에서 묵시적 조인을 사용하는 것은 좋지 않다

    * 명시적 조인, 묵시적 조인
        - 명시적 조인 : join 키워드 직접 사용
            =select m from Member m join m.team t
        - 묵시적 조인 : 경로 표현식에 의해 묵시적으로 SQL 조인 발생 (내부 조인만 가능)
            =select m.team from Member m

    경로 표현식 - 예제
        -select o.member.team from Order o -> 성공
        -select t.members from Team -> 성공
        -select t.members.username from Team t -> 실패 collection에서는 username 상태필드를 호출 할 수 없음 size만 가능
        -select m.username from Team t join t.members m -> 성공


    경로 탐색을 사용한 묵시적 조인 시 주의사항
    -항상 내부 조인
    -컬렉션은 경로 탐색의 끝, 명시적 조인을 통해 별칭을 얻어야함
    -경로 탐색은 주로 SELECT, WHERE 절에서 사용하지만 묵시적 조인으로 인해 SQL의 FROM (JOIN) 절에 영향을 줌

    !!!!!!!!!실무조언
    -가급적 묵시적 조인 대신에 명시적 조인 사용
    -조인은 SQL 튜닝에 중요 포인트
    -묵시적 조인은 조인이 일어나는 상황을 한눈에 파악하기 어려움

    */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx= em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);


            Member member = new Member();
            member.setUsername("관리자");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            Member member2 = new Member();
            member2.setUsername("관리자");
            member2.setAge(10);
            member2.setType(MemberType.ADMIN);

            member.setTeam(team);
            member2.setTeam(team);

            em.persist(member);
            em.persist(member2);

            em.flush();
            em.clear();

            // !!!!!상태필드
            //m.username에서 .을찍어서 더이상 갈 곳이 없다 이렇게 상태필드를 만나게되면 경로 탐색의 끝이라고 볼 수 있다.
            String query1 = " select m.username From Member m ";

            List<String> result = em.createQuery(query1, String.class).getResultList();

            for (String arg : result) {
                System.out.println("s = " + arg);
            }

            //!!!!!! 단일 값 연관 경로: 묵시적 내부조인 발생
            //Member에서 team이 join이 되서 team에 관한 값들을 출력하는 select query가 실행이 된다
            String query2 = "select m.team From Member m";

            List<Team> result1 = em.createQuery(query2, Team.class).getResultList();

            for (Team arg : result1) {
                System.out.println("s = " + arg);
            }

            //!!!!!! 컬렉션 값 연관경로
            //일대다 관계
            String query3 = "select t.members From Team t";

            //Collection으로 넘어오기때문에 형태를 Collection으로 지정해주어야 한다
            // 너무복잡해 잘 사용하지 않는다
            //컬렉션으로 들어가서 .을 찍어서 더 사용할 수 있는것이 size밖에 없다
            List<Collection> result2 = em.createQuery(query3, Collection.class).getResultList();
            for (Object arg : result2) {
                System.out.println("s = " + arg);
            }
            System.out.println(result2);


            String query4 = "select t.members.size From Team t";
            //컬렉션으로 들어가서 .을 찍어서 더 탐색할 수 있는것이 size밖에 없다
            Integer result3 = em.createQuery(query4, Integer.class).getSingleResult();
            System.out.println("s = " + result3);

            //컬렉션으로 들어가서 .을 찍어서 더 탐색할 수 있는것이 size밖에 없다
            //그래서 명시적인 조인으로 별칭을 사용하여 더 탐색하여 사용할 수 있다
            String query5 = "select m.username From Team t join t.members m";
            List<String> result4 = em.createQuery(query5, String.class).getResultList();
            for (String s : result4) {
                System.out.println("result4 = " +result4);
            }

            tx.commit();
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
        emf.close();

    }
}
