package hellojpa.Section2_3;

import hellojpa.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args){
        //Persistence의 unit name <persistence-unit name="hello">
        //애플리케이션 로딩지점에 딱하나만 만들어야한다
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        //고객의 요청이 올때마다 계속 껏다가 켰다가 하는것
        //쓰레드 간에 절대 공유해서는 안되고 사용하고 버려야한다
        //***JPA의 모든 데이터 변경ㅇ읜 트랙잭션안에서 실행해야 한다
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx =em.getTransaction();
        tx.begin();

        try{

            /*
            =데이터 insert=
            Member member = new Member();
            member.setId(2L);
            member.setName("HelloB"); //JPA에서는 트랜잭션이 중요하다
            //persistence에서 hibernate.show에의해서 쿼리가 보인다
            //format sql을하면 보기좋게 포맷해준다
            //use sqlcomments는 insert hellojpa.Member 가나온다
            //멤버를 저장하는 것
            em.persist(member);
            */

//            primarykey가 1인 데이터를 불러온다
//            Member findMember = em.find(Member.class , 1L);
//            System.out.println(findMember.getId());
//            System.out.println(findMember.getName());

            //삭제하기 찾은애를 넣어주면 삭제가된다
            //em.remove(findMember);
            
            //수정하기 em.persist로 저장하지않아도 수정이 바로 된다
//            findMember.setName("HelloJPA");
            //JPA를 통해서 entity를 가져오면 JPA가 관리를해서 트랜잭션을 커밋하는 시점에서
            // 변경이 됐는지 안됐는지 확인후  바꼈으면 update쿼리를 만들어서 날리고 바꿔준다


            //전체쿼리불러오기
            //em.createQuery를 사용해서 쿼리를 사용이 가능하다
            //JPA는 코드를 짤때 테이블을 대상으로 짜지않는다
            //Member객체를 대상으로 query를 한다
            
            //JPQL이란 엔티티객체를 대상으로하는 객체지향 쿼리라고 보면 된다
            //SQL은 데이터베이스 테이블을 대상으로 쿼리라고 보면된다

            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(1) //1번부터
                    .setMaxResults(8)  //8개 가져오라는 뜻
                    .getResultList();

            //iter을 사용해주면 자동으로 for문이 생성이 된다
            for (Member member : result) {
                System.out.println("member.name = " + member.getName());
            }

            //커밋을 꼭해주지 않으면 바꿔도 반영이 되지 않는다
            tx.commit();

        }catch (Exception e){
            tx.rollback();

        }finally {
            //code 사용을 다하고나면 꼭 닫아줘야한다
            em.close();
        }

        emf.close();
    }
}
