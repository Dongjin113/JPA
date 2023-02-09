package hellojpa.Section9ValueType.ValueTypeImmutabilityObject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ValueTypeMain {
    /*값 타입은 복잡한 객체 세상을 조금이라도 단순화하려고 만든 개념이다,
    따라서 값 타입은 단순하고 안전하게 다룰 수 있어야 한다.
    
    값타입 공유 참조
    -임베디드 타입 값은 값 타입을 여러 엔티티에서 공유하면 위험함
    -부작용(side effect)발생

    객체 타입의 한계
    기본 타입(primitive type)
    int a = 10;
    int b = a; // 기본 타입은 값을 복사
    b = 4;
    b는 a의 값이 복사되어 들어가기떄문에 b의 값을 바꾸면 b의 값만 바뀌고 a의 값은 변경되지 않는다.
    a를 변경해도 마찬가지로 b는 10이되고 a는 4가 된다

    객체 타입
    Address a = new Address("Old");
    Address b = a; //객체 타입은 참조를 전달
    b.setCity("New")
     a,b 가 모두 같은 인스턴스를 가르키기 떄문에 b의 값을 바꾸는 순간 a와b 모두가 변경이 된다
    */

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Address address = new Address("city","street","10000");

            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(address);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setHomeAddress(address);
            em.persist(member2);

            //한가지 member만 바뀌는게 아니라 member2까지 같이 바뀐다
            //임베디드 값타입을 여러 엔티티에서 공유하면 굉장히 위험하다 같이공유해서 사용하고 싶다면 엔티티로 만들어서 사용해야 한다
//            member.getHomeAddress().setCity("newCity");


            /* 값 타입을 복사
            -값 타입의 실제 인스턴스인 값을 공유하는 것은 위험
            -대신 값(인스턴스)를 복사해서 사용
            */

            Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());
            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setHomeAddress(copyAddress);
            em.persist(member3);

            member.getHomeAddress().setCity("newCity");

            /* 객체 타입의 한계
            -항상 값을 복사해서 사용하면 공유 참조로 인해 발생하는 부작용을 피할 수 있다
            -문제는 임베디드 타입처럼 직접 정의한 값 타입은 자바의 기본 타입이 아니라 객체 타입이다
            -자바 기본타입에 값을 대입하면 값을 복사한다.
            -객체 타입은 참조 값을 직접 대입하는 것을 막을 방법이 없다
            - 객체의 공유 참조는 피할 수 없다.

            객체 타입은 참조를 복사해서 넘긴다 참조를 복사해도 한인스턴스를 사용해 같이 변경이될 수 있기떄문에
            참조를 막을수있는 방법이 없어 객체 타입을 불변 객체로 만들어준다

            *불변 객체 : 생성 시점 이후 절대 값을 변경할 수 없는 객체

                - 객체 타입을 수정할 수 없게 만들면 부작용을 원천 차단
                - 값 타입은 불변 객체(immutable object)로 설계해야함
                -불변 객체: 생성 시점 이후 절대 값을 변경할 수 없는 객체
                -생성자로만 값을 설정하고 수정자(Setter)를 만들지 않으면 됨
                -참고: Integer, String은 자바가 제공하는 대표적인 불변 객체

            !! 불변이라는 작은 제약으로 부작용이라는 큰 재앙을 막을 수 있다
            */

            //불변 address
            ImmutaionAddress ImuAddress = new ImmutaionAddress("city","street","10000");

            Member member4 = new Member();
            member4.setUsername("member4");
            member4.setImuAddress(ImuAddress);
            em.persist(member4);

            //불변 address의 값을 바꾸는 방법 값을 통으로 바꿔 끼워야한다
            ImmutaionAddress newAddress = new ImmutaionAddress("NewCity",ImuAddress.getStreet(),ImuAddress.getZipcode());

            member4.setImuAddress(newAddress);




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
