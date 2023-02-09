package hellojpa.Section8ProxyAndAssociationMapping.EagerLazyLoading.LazyLoading;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class LoadingMain {

    public static void main(String[] args){


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);


            Member member1 = new Member();
            member1.setName("member1");
            member1.setTeam(team);
            em.persist(member1);

            em.flush();
            em.clear();

            Member m = em.find(Member.class, member1.getId());

            // 지연로딩을 설정을하면 연관된 것을 프록시로 가져온다
            System.out.println("m = "+m.getTeam().getClass());

            //멤버를 가져올때는 딱 member만 가져오고 team은  프록시로 가져온다
            //이다음에 team에있는 무언가를 터치를 그시점에 team쿼리가 나가게된다

            System.out.println("======================");
            m.getTeam().getName();
            System.out.println("======================");

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

/*

 ㄹㅇㅁ


 */