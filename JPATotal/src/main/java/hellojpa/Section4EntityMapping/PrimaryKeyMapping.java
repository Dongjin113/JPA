package hellojpa.Section4EntityMapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class PrimaryKeyMapping {

    public static void main(String[] args){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();;

        try {

            PKMember member = new PKMember();
            PKMember member1 = new PKMember();
            PKMember member2 = new PKMember();
            member.setUsername("C");
            member1.setUsername("B");
            member2.setUsername("A");

            System.out.println("===================");
            System.out.println("member.id2 = "+ member.getId()); //생성전의 값은 null DB에 넣기전까지 값을 알 수 없다
            em.persist(member); //1, 51호출
            em.persist(member1); //메모리내에서 2 51까지 다시 호출하지않음
            em.persist(member2); //메모리내에서 3 51까지 다시 호출하지않음



            //SEQUENCE는 DB에서 다음 pk 값인 SEQUENCE 값을 call로 불러온후 영속성 컨텍스에 넣어준다
            //자꾸 next call로 불러오게되면 어쨋든 네트워크를 타기 때문에 성능 문제가 생길수있어
            //최적화 하기위해 미리 50개를 땡겨오기위해 allocationSize의 default 값은 50으로 설정이 되어있다.
            //50개를 미리땡겨오고 1번부터 50번까지 다사용하면 다음 call로 51~100번까지 가져와서 다시 사용한다
            System.out.println("member.id2 = "+ member.getId());
            System.out.println("===================");

            //SEQUENCE는 commit하는 시점에 insert 쿼리가 실행이 된다
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();


    }
}
