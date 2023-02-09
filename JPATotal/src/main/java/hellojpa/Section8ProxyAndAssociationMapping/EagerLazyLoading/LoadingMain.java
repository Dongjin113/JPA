package hellojpa.Section8ProxyAndAssociationMapping.EagerLazyLoading;


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
            System.out.println(m.getTeam().getName());
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

    Lazy 지연로딩
    Member을 사용할때 Team을 잘 사용하지않으면 성능 최적화를 위해 Member와 Team을 따로 불러오기 만들기위해 사용한다

    EAGER 즉시로딩
    Member을 불러올때 조인해서 Team도 같이 불러오게 한다


    !!!! 실무에서는 즉시로딩을 사용하면 안된다
    - 가급적 지연 로딩만 사용(특히 실무에서)
    - 즉시 로딩을 적용하면 예상하지 못한 SQL이 발생
    - 즉시 로딩은 JPQL에서 N+1 문제를 일으킨다.
    - @ManyToOne, @OneToOne은 기본이 즉시 로딩 -> LAZY로 설정
    - @OneToMany , @ManyToMany는 기본이 지연 로딩

    지연 로딩 활용
    Member와 Team은 자주 함께 사용 -> 즉시로딩
    Member와 Order는 가끔 사용 -> 지연로딩
    Order와 Product는 자주 함께 사용 -> 즉시 로딩

    !!!!!그러나 실무에서는 무조건 지연로딩을 사용한다


    지연로딩 활용 - 실무
    - 모든 연관관계에 지연 로딩을 사용해라!
    - 실무에서 즉시 로딩을 사용하지 마라!
    - JPQL fetch조인이나, 엔티티 그래프 기능을 사용해라!(뒤에서 설명)
    - 즉시 로딩은 상상하지 못한 쿼리가 나간다

    

 */