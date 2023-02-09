package hellojpa.Section10JPQLBasicGrammar.BasicGrammarQueryApi;

import javax.persistence.*;
import java.util.List;

public class JPQLMain {
    /* JPQL 소개
    -JPQL은 객체지향 쿼리 언어다. 따라서 테이블을 대상으로 쿼리 하는 것이 아니라 엔티티 객체를 대상으로 쿼리한다
    -JPQL은 SQL을 추상화해서 특정데이터베이스 SQL에 의존하지 않는다.
    -JPQL은 결국 SQL로 변환된다

    JPQL 문법
    - select m from Member as m where m.age>18
    - 엔티티와 속성은 대소문자 구분O (Member, age)
    - JPQL 키워드는 대소문자 구분 X (SELECT, FROM, wher)
    - 엔티티 이름 사용, 테이블 이름이 아님(Member)
    - 별칭은 필수(m) (as는 생략가능)

    TypeQuery, Query
    TypeQuery: 반환 타입이 명확할 때 사용
    Query : 반환 타입이 명확하지 않을 때 사용

    결과 조회API
    -query.getResultList(): 결과가 하나 이상일 때, 리스트 반환
        -결과가 없으면 빈 리스트 반환
    -query.getSingleResult(): 결과가 정확히 하나, 단일 객체 반환
        -결과가 없으면 :javax.persistence.NoResultException
        -둘 이상이면 : javax.persistence.NonUniqueResultException
    */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em =emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);


            /* TypeQuery, Query
            TypeQuery: 반환 타입이 명확할 때 사용
            Query : 반환 타입이 명확하지 않을 때 사용
            */

            TypedQuery<Member> query1 = em.createQuery("select m from Member m ", Member.class);
            //username의 타입이 String이라 String으로 타입을 지정해줌
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m ", String.class);
            //타입정보를 명확하게 명기할수 없을경우 username과 age의 타입이 달라 타입정보를 명기할 수 없음
            //이때는 Query를 사용
            Query query3 = em.createQuery("select m.username, m.age from Member m ");

            //결과가 여러개일때
            //결과가 없으면 빈리스트를 반환
            List<Member> resultList1 = query1.getResultList();
            for (Member member1 : resultList1) {
                System.out.println("member1 "+member1);
            }
            //결과가 한가지일 때 결과가 정확히 한개 일때만 사용해야한다
            TypedQuery<Member> query4 = em.createQuery("select m from Member m where m.id = 1", Member.class);
            Member singleResult1 = query4.getSingleResult();
            System.out.println("singleResult1"+ singleResult1);

            //파라미터 바인딩 이름기준 :를 사용한다
            TypedQuery<Member> query5 = em.createQuery("select m from Member m where m.username = :username", Member.class);
            query5.setParameter("username","member1");
            Member parameterBinding = query5.getSingleResult();
            System.out.println("parameter "+ parameterBinding.getUsername());

            Member query6 = em.createQuery("select m from Member m where m.username = :username", Member.class)
            .setParameter("username","member1")
            .getSingleResult();
            System.out.println("parameterchain "+ query6.getUsername());



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
