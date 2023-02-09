package hellojpa.Section11JPQLMiddleGrammar.FetchJoin.FetchJoin1;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class FetchMain {

    /* 패치 조인(fetch join) : query로 내가원하는대로 어떤 객체 그래프를 한번에 조회할 꺼야 라는것을 직접 명시적으로 동적인 타이밍에 정할 수 있는것
    - SQL 조인 종류X
    - JPQL에서 성능 최적화를 위해 제공하는 기능
    - 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능
    - join fetch 명령어 사용
    - 페치조인 :: = [LEFT [OUTER] | INNER ] JOIN FETCH 조인경로

    엔티티 페치 조인
    - 회원을 조회하면서 연관된 팀도 함께 조회(SQL 한 번에)
    - SQL을 보면 회원 뿐만 아니라 팀(T.*)도 함께 SELECT

    -[JPQL]
        = select m from Member m join fetch m.team
    -[SQL]
        = SELECT M.*, T.* FROM MEMBER M INNER JOIN TEAM T ON M.TEAM_ID = T.ID

    컬렉션 페치 조인
        - 일대다 관계, 컬렉션 페치조인
        -[JPQL]
          = select t
            from Team join fetch t.members
            where t.name ='팀A'

        -[SQL]
          = SELECT T.*, M.*
            FROM TEAM T
            INNER JOIN MEMBER M ON T.ID = M.TEAM_ID
            WHERE T.NAME = '팀A'

        페치 조인과 DISTINCT
        -SQL의 DISTINCT는 중복된 결과를 제거하는 명령
        -JPQL의 DISTINCT 2가지 기능 제공
            =1.SQL에 DISTINCT를 추가
            =2.애플리케이션에서 엔티티 중복 제거
        - select distinct t
          from Team t join fetch t.members
          where t.name = '팀A'
        -SQL에 DISTINCT를 추가하지만 데이터가 다르므로 SQL결과에서 중복제거 실패
        -DISTINCT가 추가로 애플리케이션에서 중복 제거시도
        -같은 식별자를 가진 Team 엔티티 제거

        페치 조인과 일반 조인의 차이
        -일반 조인 실행시 연관된 엔티티를 함께 조회하지 않음
        -[JPQL]
            = select t from Team t joint t.members m
              where t.name = '팀A'
        -[SQL]
            = select T.*
              From TEAM T
              INNER JOIN MEMBER M ON T.ID = M.TEAM_ID
              WHERE T.NAME = '팀A'
        - JPQL은 결과를 반활할 때 연관관계 고려X
        - 단지 SELECT 절에 지정한 엔티티만 조회할 뿐
        - 여기서는 팀 엔티티만 조회하고, 회원 엔티티는 조회X
        - 페치 조인을 사용할 때만 연관된 엔티티도 함께 조회(즉시 로딩)
        - 페치 조인은 객체 그래프를 SQL 한번에 조회하는 개념
            
    */

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx= em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);


            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);



            em.flush();
            em.clear();

            String query = "select m From Member m";

            List<Member> result = em.createQuery(query, Member.class).getResultList();

            for (Member member : result) {
                System.out.println("member = " + member.getUsername() +" , " +member.getTeam().getName());
//              @ManyToOne(fetch = FetchType.LAZY)라서
//              회원1, 팀A(SQL)
//              회원2, 팀A(1차캐시 영속성 컨텍스트)
//              회원3, 팀B(SQL) 영속에 없음
//              회원 100명 (팀소속이 전부 다르다면) -> SQL이 N+1번 실행된다
//              그래서 페치 조인을 사용한다
            }

            System.out.println("===========fetch===========");
            //페치조인 사용하기
            //join으로 쿼리가 나가서 데이터를 한방에 가져와 영속성 컨텍스트에 받아옴
            //프록시가 아닌 실제데이터를 받아옴
            //지연로딩이 전혀없이 깔끔하게 가져옴
            String fetchQuery = "select m From Member m join fetch m.team";

            List<Member> fetchResult = em.createQuery(fetchQuery, Member.class).getResultList();

            for (Member member : fetchResult) {
                //페치조인으로 회원과 팀을 함께 조회해서 지연로딩 X 주로 조회할 때 많이사용
                //지연로딩을 설정해도 페치조인이 우선순위라 실행이된다
                System.out.println("member = " + member.getUsername() +" , " +member.getTeam().getName());
            }

            System.out.println("=================collection fetch===================");
            //컬렉션 페치 조인
            //일대다 관계, 컬렉션 페치 조인
            String colQueryFet1 = "select t From Team t join fetch t.members";

            List<Team> colFetchResult1 = em.createQuery(colQueryFet1,Team.class).getResultList();
            for (Team team : colFetchResult1) {
                //리스트의 크기만큼 값이 중복되서 출력이된다
                System.out.println("team = " + team.getName() + " , "+ team.getMembers().size());
                    for(Member member : team.getMembers()){
                        System.out.println("  --> member =" + member);
                    }
            }

            System.out.println("=================중복 제거 collection fetch===================");
            //중복된 값을 제거하기
            String colQueryFet2 = "select distinct t From Team t join fetch t.members";

            List<Team> colFetchResult2 = em.createQuery(colQueryFet2,Team.class).getResultList();
            for (Team team : colFetchResult2) {
                //리스트의 크기만큼 값이 중복되서 출력이된다
                System.out.println("team = " + team.getName() + " , "+ team.getMembers().size());
                for(Member member : team.getMembers()){
                    System.out.println("  --> member =" + member);
                }
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
