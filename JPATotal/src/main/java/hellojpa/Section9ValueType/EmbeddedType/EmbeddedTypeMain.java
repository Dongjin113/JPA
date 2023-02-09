package hellojpa.Section9ValueType.EmbeddedType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class EmbeddedTypeMain {
    /* 임베디드 타입
        -새로운 값 타입을 직접 정의할 수 있음
        -JPA는 임베디드 타입(embedded type)이라 함
        -주로 기본 값 타입을 모아서 만들어서 복합 값 타입이라고도 함
        - int, String과 같은 값 타입
        - 회원 엔티티는 이름, 근무 시작일, 근무 종료일, 주소 도시, 주소 번지, 주소 우편번호를 가진다.
            =회원 엔티티는 이름, 근무기간, 집 주소를 가진다
            Member(Entity) : id, name
            Period(Value Type) : startDate, endDate
            Address(Value Type) : city, street, zipcode
            
       임베디드 타입 사용법
       -@Embeddable: 값 타입을 정의하는 곳에 표시
       -@Embedded: 값 타입을 사용하는 곳에 표시
       -기본 생성자 필수

       임베디드 타입의 장점
       -재사용
       -높은 응집도
       -Period.isWork()처럼 해당 값 타입만 사용하는 의미 있는 메소드를 만들 수 있음
       -임베디드 타입을 포함한 모든 값 타입은, 값 타입을 소유한 엔티티에 생명주기를 의존함

        @AttributeOverride: 속성 재정의
        -한 엔티티에서 같은 값 타입을 사용하면?
        -컬럼 명이 중복됨
        -@AttributeOverrides, @AttributeOverride를 사용해서 컬럼 명 속성을 재정의

    */

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("hello");
            member.setHomeAddress(new Address("city","street", "100"));
            member.setWorkPeriod(new Period());

            em.persist(member);

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
