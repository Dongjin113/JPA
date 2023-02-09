package hellojpa.Section7LuxuriousMapping.InheritanceRelationMapping.InheritanceMapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class InheritanceMappingMain {
    /* 상속관계 매핑
     - 관계형 데이터베이스는 상속관계X
     - 슈퍼타입 서브타입 관계라는 모델링 기법이 객체 상속과 유사
     - 상속관계 매핑: 객체의 상속과 구조와 DB의 슈퍼타입 서브타입 관계를 매핑
        =슈퍼타입 서브타입 논리 모델을 실제 물리 모델로 구현하는 방법
        =각각 테이블로 변환 -> 조인 전략
        =통합 테이블로 변환 -> 단일 테이블 전략
        =서브타입 테이블로 변환 -> 구현 클래스마다 테이블 전략

        @Inheritance(strategy=InheritanceType.XXX)
            =JOINED : 조인전략
            =SINGLE_TABLE: 단일 테이블 전략
            =TABLE_PER_CLASSL: 구현 클래스마다 테이블 전략
        @DiscriminatorColumn(name="DTYPE")
        @DiscriminatorValue("XXX")

=============조인테이블 전략====================
        조인 전략의 장점
            -정규화가 되어있다 데이터가 정규화도 되어있고 제약조건 같은것을 Item에 걸어서 맞출수가 있다
            - 테이블 정규화
            - 외래 키 참조 무결성 제약조건 활용가능
            - 저장공간 효율화
        
        조인 테이블 단점
            -조회시 조인을 많이 사용, 성능 저하
            -조회 쿼리가 복잡함
            -데이터 저장시 INSERT SQL 2번 호출

=========================단일 테이블 전략======================
        단일 테이블 장점
            - 조인시 필요 없으므로 일반적으로 조회 성능이 빠름
            - 조회 쿼리가 단순함

        단일 테이블 단점
        - 자식 엔티티가 매핑한 컬럼은 모두 null 허용
        - 단일 테이블에 모든 것을 저장하므로 테이블이 커질 수 있고 상황에 따라서 조회 성능이 오히려 느려질 수 있다.

======================구현 클래스마다 테이블 전략=================
        !! 이 전략은 데이터베이스 설계자와 ORM 전문가 둘 다 추천X
        구현 클래스 장점
            -서브 타입을 명확하게 구분해서 처리할 때 효과적
            -not null 제약조건 사용 가능
        
        구현 클래스 단점
            -여러 자식 테이블을 함께 조회할 때 성능이 느림(UNION SQL)
            -자식 테이블을 통합해서 쿼리하기 어려움

===============================================================
    상속관계 매핑
        -관계형 데이터베이스는 상속 관계X
        -슈퍼타입 서브타입 관계라는 모델링 기법이 객체 상속과 유사
        -상속관계 매핑: 객체의 상속과 구조와 DB의 슈퍼타입 서브타입 관계를 매핑

     */

    public static void main(String[] args){
        EntityManagerFactory emf= Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {


           /*
           ===PAKAGE - OneParentThreeChild===
           //각각의 테이블에 알맞게 들어간다
            Movie movie = new Movie();
            movie.setDirector("a");
            movie.setActor("BB");
            movie.setName("바함살");
            movie.setPrice(10000);
            em.persist(movie);

            //조회는 join 해서 가져온다

            em.clear();
            em.flush();

            Movie findMove = em.find(Movie.class, movie.getId());
            //Movie와 Item을 inner join을 해서 가져온다
            System.out.println(findMove);*/

            //각각의 테이블에 알맞게 들어간다
            Movie movie = new Movie();
            movie.setDirector("a");
            movie.setActor("BB");
            movie.setName("바함살");
            movie.setPrice(10000);
            em.persist(movie);

            //조회는 join 해서 가져온다

            em.clear();
            em.flush();

            Movie findMove = em.find(Movie.class, movie.getId());
            //Movie와 Item을 inner join을 해서 가져온다
            System.out.println(findMove);


            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();



    }
}
