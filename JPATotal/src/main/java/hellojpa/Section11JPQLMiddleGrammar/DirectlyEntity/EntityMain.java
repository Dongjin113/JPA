package hellojpa.Section11JPQLMiddleGrammar.DirectlyEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class EntityMain {
    /* 엔티티 직접 사용 - 기본 키 값
     -JPQL 에서 엔티티를 직접 사용하면 SQL 에서 해당 엔티티의 기본키 값을 사요
     -[JPQL]
        =select count(m.id) from Member m //엔티티의 아이디를 사용
        =select count(m) from Member m //엔티티를 직접 사용
     -[SQL] (JPQL 둘다 같은 다음 SQL 실행)
        select count(m.id) as cnt from Member m

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

            String query1 = "select m from Member m where m = :member";

            //엔티티를 파라미터로 전달
            List<Member> findMember = em.createQuery( query1 , Member.class)
                    .setParameter("member" , member1)
                    .getResultList();

            for (Member member : findMember) {
                System.out.println("member = "+ member);
            }

            System.out.println("============식별자 직접전달============");
            //식별자를 직접 전달
            String query2 ="select m from Member m where m.id = :memberId";

            List<Member> findMemberId = em.createQuery(query2, Member.class)
                    .setParameter("memberId", member1.getId())
                    .getResultList();
            for (Member member : findMemberId) {
                System.out.println("member = " + member);
            }
            System.out.println("============fk 외래키 값============");

            String fk1 ="select m from Member m where m.team = :team";

            List<Member> findfk = em.createQuery(fk1, Member.class)
                    .setParameter("team", teamA)
                    .getResultList();
            for (Member member : findfk) {
                System.out.println("member = "+ member);

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
