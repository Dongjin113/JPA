package hellojpa.Section10JPQLBasicGrammar.Paging;


import javax.persistence.*;
import java.util.List;

public class PagingMain {
    /* 페이징 API
    - JPA는 페이징을 다음 두 API로 추상화
    - setFirstResult(int startPosition) : 조회 시작 위치(0부터 시작)
    - setMaxResults(int maxResult) : 조회할 데이터 수

    */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx= em.getTransaction();
        tx.begin();

        try {
            for(int i =0 ; i<100; i++){
                Member member = new Member();
                member.setUsername("member1");
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();
            System.out.println("result size = "+ result.size());
            for (Member member1 : result) {
                System.out.println("member1 = "+ member1);
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
