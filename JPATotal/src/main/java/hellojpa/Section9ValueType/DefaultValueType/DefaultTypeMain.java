package hellojpa.Section9ValueType.DefaultValueType;

import hellojpa.Section8ProxyAndAssociationMapping.EagerLazyLoading.Member;
import hellojpa.Section8ProxyAndAssociationMapping.EagerLazyLoading.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class DefaultTypeMain {

    public static void main(String[] args){
/*

        int a = 10;
        int b = a;

        a=20;
        System.out.println("a "+ a); //a의 값은 변경이 되나
        System.out.println("b "+ b); //b의 값은 a의 시점이 복사가 되서 저장이 되기때문에 값이 변경되지 않는다

        //그러나 Integer는 레퍼런스로 넘어가서 같은 인스턴스를 공유하기 때문에 a와 b의 값이 동시에 바뀌게 된다
*/

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

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
    데이터 타입의 분류

    *엔티티 타입
     - @Entity로 정의하는 객체
     - 데이터가 변해도 식별자로 지속해서 추적 가능
     - 예) 회원 원티티의 키나 나이 값을 변경해도 식별자로 인식 가능

     * 값 타입
     - int, Integer, String처럼 단순히 값으로 사용하는 자바 기본 타입이나 객체
     - 식별자가 없고 값만 있으므로 변경시 추적불가
     - 예)숫자 100을 200으로 변경하면 완전히 다른 값으로 대체

    * 값타입 분류
        -기본 값타입
            =자바 기본 타입(int, double)
            =래퍼 클래스(Integer, Long)
            =String
        -임베디드 타입(embedded type, 복합 값 타입) ex) 좌표 같은것을 묶어서 사용하고 싶을때
        -컬렉션 값 타입(collection  value type) Java collection 에 기본이나 인베디드 값타입을 넣을수 있는것

    *기본 값타입
        -예): String name, int age
        -생명 주기를 엔티티의 의존
            =예)회원을 삭제하면 이름, 나이 필드도 함께 삭제
        -값 타입은 공유하면 X
            =예)회원 이름 변경시 다른 회원의 이름도 함께 변경되면 안됨

    참고: 자바의 기본타입은 절대 공유 X
        -int, double 같은 기본 타입(primitive type)은 절대 공유 X
        -기본 타입은 항상 값을 복사함
        -Integer같은 래퍼 클래스나 String 같은 특수한 클래스는 공유 가능한 객체이지만 변경 X


*/
