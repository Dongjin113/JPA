package hellojpa.Section11JPQLMiddleGrammar.FetchJoin.FetchJoin2;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class FetchMain {

    /* 패치 조인의 특징과 한계
        -페치 조인 대상에는 별칭을 줄 수 없다.
            =하이버네이트는 가능, 가급적 사용X
        -둘 이상의 컬렉션은 페치 조인 할 수 없다.
        -컬렉션을 페치 조인하면 페이징 API(setFirstResult, setMaxResults)를 사용할 수 없다.
            = 일대일,다대일 같은 단일 값 연관 필드들은 페치 조인해도 페이징 가능
            = 하이버네이트는 경고 로그를 남기고 메모리에서 페이징(매우 위험)
        -연관된 엔티티들을 SQL 한 번으로 조회 - 성능, 최적화
        -엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선함
            = @OneToMany(fetch = FetchType.LAZY) //글로벌 로딩 전략
        -실무에서 글로벌 로딩 전략은 모두 지연로딩
        -최적화가 필요한 곳은 페치 조인 적용
        
        페치 조인 - 정리
        - 모든 거을 페치 조인으로 해결할 수는 없음
        - 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
        - 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 하면,
          페치 조인 보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적
            
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

            System.out.println("===========fetch===========");
            //일대다
//            String query ="select t From Team t join fetch t.members";

            //방법1 . 일대다의 경우 다대일로 뒤집어서 페이징을 사용하면 된다
            //다대일
            String fetchQuery = "select m From Member m join fetch m.team";

            List<Member> fetchResult = em.createQuery(fetchQuery, Member.class).getResultList();

            for (Member member : fetchResult) {
                System.out.println("member = " + member.getUsername() +" , " +member.getTeam().getName());
            }

            //방법2 . Entitiy에서 BatchSize라는것을 설정한다
            //lazy로 발생하는 쿼리를 한번에 입력한수치만큼 묶어서 가져온다
            //결과적으로 쿼리가 n+1이 아니라 딱 테이블 수만큼 맞출 수 있다
            System.out.println("=============batchsize============");
            String batchQuery = "select t From Team t";

            List<Team> batchResult = em.createQuery(batchQuery, Team.class).getResultList();

            for (Team team : batchResult) {
                System.out.println("team = " + team.getName() +" , " + team.getMembers());
                for (Member member : team.getMembers()){
                    System.out.println("=> member = "+ member);
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
