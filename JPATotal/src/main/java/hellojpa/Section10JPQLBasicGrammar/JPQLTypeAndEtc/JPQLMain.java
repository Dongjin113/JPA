package hellojpa.Section10JPQLBasicGrammar.JPQLTypeAndEtc;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JPQLMain {
    /* JPQL 타입 표현
    - 문자 :'HELLO' , 'She','s'
    - 숫자 : 10L(Long), 10D(Double), 10F(Float)
    - Boolean : TRUE, FALSE
    - ENUM : jpabook.MemberType,Admin(패키지명 포함)
    - 엔티티 타입: TYPE(m) = Member (상속 관계에서 사용)

    JPQL 기타
    -SQL과 문법이 같은 식
    -EXISTS, IN
    -AND, OR, NOT
    - =, >, >=, <, <=, <>
    - BETWEEN, LIKE, IS NULL
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
            member.setUsername("member");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            /*String query = "select m.username, 'HELLO' , True From Member m "
                    + "where m.type = hellojpa.Section10JPQLBasicGrammar.JPQLTypeAndEtc.MemberType.USER";
            List<Object[]> result = em.createQuery(query).getResultList();*/

            //파라미터 바인딩을 사용하여 pakage명을 줄여준다
            String query = "select m.username, 'HELLO' , True From Member m "
                    + "where m.type = :userType";
            List<Object[]> result = em.createQuery(query)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();

            for (Object[] objects : result) {
                System.out.println("objects = "+ objects[0]);
                System.out.println("objects = "+ objects[1]);
                System.out.println("objects = "+ objects[2]);
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
