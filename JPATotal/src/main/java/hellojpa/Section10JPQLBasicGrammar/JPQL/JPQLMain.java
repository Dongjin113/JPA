package hellojpa.Section10JPQLBasicGrammar.JPQL;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JPQLMain {
    /* JPQL 소개
    - 가장 단순한 조회 바업
        = Entitiy Manager.find()
        = 객체 그래프 탐색 (a.getB().getC())
    - 나이가 18살 이상인 회원을 모두 검색하고 싶다면?


    *JPQL
        -JPA를 사용하면 엔티티 객체를 중심으로 개발
        -문제는 검색 쿼리
        -검색을 할 떄도 테이블이 아닌 엔티티 객체를 대상으로 검색
        -모든 DB데이터를 객체로 변환해서 검색하는 것은 불가능
        -애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요
        -JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어 제공
        -SQL과 문법 유사, SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 지원
        -JPQL은 엔티티 객체를 대상으로 쿼리
        -SQL은 데이터베이스 테이블을 대상으로 쿼리
        -테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
        -SQL을 추상화해서 특정 데이터베이스 SQL에 의존 X
        -JPQL을 한마디로 정의하면 객체 지향 SQL
    */

        /* Criteria 소개
    -문자가 아닌 자바코드로 JPQL을 작성할 수 있음
    -JPQL 빌더 역할
    -JPA 공식기능
    - 단점: 너무 복잡하고 실용성이 없다
    - Criteria 대신에 QueryDSL 사용 권장
    */

    /* 네이티브 SQL 소개
    - JPA가 제공하는 SQL을 직접 사용하는 기능
    - JPQL로 해결할 수 없는 특정 데이터베이스에 의존적인 기능
    - 예)오라클 CONNECT BY, 특정 DB만 사용하는 SQL 힌트
    */
    /* JDBC 직접 사용, SpringJdbcTemplate 등
    - JPA를 사용하면서 , JDBC 커넥션을 직접 사용하거나, 스프링 JdbcTemplate, 마이바티스등을 함께 사용가능
    - 단 영속성 컨텍스트를 적절한 시점에 강제로 플러시 필요
    - 예) JPA를 우회해서 SQL을 실행하기 직전에 영속성 컨텍스트 수동 플러시
    */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx= em.getTransaction();
        tx.begin();
        try {
            //(Query문, Type)
//            em.createQuery("select m From Member m where m.username like '%Kim%'", Member.class)
//                            .getResultList();


            
            //criteria 사용준비
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            Root<Member> m = query.from(Member.class);

//            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"),"kim"));
//          --> 동적쿼리로 나타내고 싶을 때
            CriteriaQuery<Member> cq = query.select(m);

            String username = "dsfads";
            if(username != null){
               cq  = cq.where(cb.equal(m.get("username"),"kim"));
            }

            List<Member> resultList = em.createQuery(cq).getResultList();


            em.createQuery("select MEMBER_ID, city ,street, USERNAME from MEMBER").getResultList();


            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

            List<Member> resultList1 = em.createNativeQuery("select MEMBER_ID, city , street, zipcode from, USERNAME from Member ", Member.class)
                            .getResultList();

            //flush -> commit, query
            //JPA 기술을 사용한다면 쿼리가 날아가기전에도 기본적으로 flush가 자동으로 된다
            //dbconn.executeQuery("select * from member")은 JPA와 아무런 관계가 없어 flush가 자동으로 되지않아
            // 강제로 flush를 하지않으면 데이터를 읽어오지 못한다
            // JPA를 우회에서 SQL을 실행하기 직전에 영속성 컨텍스트를 꼭 수동 플러쉬 해줘야한다
            for (Member member1 : resultList1) {
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
