package hellojpa.Section6VarietyAssociationMapping.VarietyAssociationMapping.OneToManyMapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class OneToManyMain {

    public static void main(String[] args){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");

            em.persist(member);

            Team team = new Team();
            team.setName("TeamA");
            team.getMembers().add(member);
            em.persist(team);

            /*
            update Member set TEAM_ID=? where MEMBER_ID=?
            업데이트 쿼리가 추가로나간다 그 이유는
            Team 엔티티를 저장하는데 Member 테이블의 Team의ID를 변경해야하기 때문에

           */

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();

    }
}
