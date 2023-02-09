package hellojpa.Section9ValueType.ValueTypeCollection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

public class ValueTypeCollection {
    /*값 타입 컬렉션이란?
    값타입을 컬렉션에 담아서 사용하는 것
    
    값타입 컬렉션
        - 값 타입을 하나 이상 저장할 때 사용
        -맵핑 할때에는 @ElementCollection , @CollectionTable 사용
        -데이터베이스는 컬렉션을 같은 테이블에 저장할 수 없다.
            =정석적으로는 방법잉 없어 꼼수로 일대다로 풀어서 별도의 테이블로 만들어 내야한다
            =그래서 join키를 넣어서 만들어준다
        -컬렉션을 저장하기 위한 별도의 테이블이 필요함

    */

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("Member1");
            member.setHomeAddress(new Address("homecity1","street","10000"));

            //HashSet에 값이 들어간ㄷ
            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

//            member.getAddressHistory().add(new Address("old1","street","10000"));
//            member.getAddressHistory().add(new Address("old2","street","10000"));
            member.getAddressHistory().add(new AddressEntity("old1","street","10000"));
            member.getAddressHistory().add(new AddressEntity("old2","street","10000"));


            em.persist(member);
            /*생명주기가 다 Member에 소속된다
            collection도 다른테이블임에도 불구하고 라이프  값타입이기 떄문에 같이 돌아간다
            값타입 collection들도 크게보면 값타입이다
            */

            em.flush();
            em.clear();

            System.out.println("=============START=================");
            Member findMember = em.find(Member.class, member.getId());
            //멤버만 가져온다 이컬렉션들은 다 지연로딩이라는 뜻이다

            /*값 타입 컬렉션 사용
            - 값 타입 저장예제
            - 값 타입 조회 예제
                = 값 타입 컬렉션도 지연 로딩 전략 사용
            - 값 타입 수정 예제
            - 참고: 값 타입 컬렉션은 영속성 전이(Cascade) + 고아 객체 제거 기능을 필수로 가진다고 볼 수 있다*/
/*              값타입 조회
            List<Address> addressHistory = findMember.getAddressHistory();
            for (Address address : addressHistory) {
                System.out.println("address = "+ address.getCity());
            }
            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for (String favoriteFood : favoriteFoods) {
                System.out.println("favoirteFood = " + favoriteFood);
            }*/

//            homeCity -> newCity 값타입을 이런식으로 변경하면 절대 안된다
//            findMember.getHomeAddress().setCity("newCity22");

            Address a = findMember.getHomeAddress();
            //이런식으로 완전히 새로운 인스턴스로 갈아껴야한다
            findMember.setHomeAddress(new Address("newCity", a.getStreet(),a.getZipcode()));

            //치킨 -> 한식 값타입(String) 이기 떄문에 치킨을 삭제하고 다시 추가해줘야한다
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");


            
            //실무에서는 사용하지 않는다
            //equals로 값을 비교해서 삭제됨으로 Address의 equals와 hash코드를 제대로 구현해주어야 한다
            //값타입 컬렉션에 변경이 일어나면 값을 한가지만 찾아서 삭제하는것이 아니라 모든데이터를 삭제하고 값타입 컬렉션에 있는 현재값을 모두 다시 저장한다
            /* 결론적으로는 원하는대로 잘 바꼈으나
            값 타입 컬렉션의 제약사항
                -값 타입은 엔티티와 다르게 식별자 개념이 없다
                -값은 변경하면 추적이 어렵다
                -값 타입 컬ㄹ게션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고,
                    값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.
                -값 타입 컬렉션을 매핑하는 테이블은 모두 컬럼을 묵어서 기본키를 구성해야 함 : null 입력 X, 중복 저장 X
            */
            findMember.getAddressHistory().remove(new AddressEntity("old1","street","10000"));
            findMember.getAddressHistory().add(new AddressEntity("newCity1","street","10000"));

            /*
            값 타입 컬렉션 대안
            -실무에서는 상황에 따라 값 타입 컬렉션 대신에 일대다 관계를 고려
            -일대다 관계를 위한 엔티티를 만드고, 열기에서 값 타입을 사용
            -영속성 전이(Cascade) + 고아 객체 제거를 사용해서 값 타입 컬렉션 처럼 사용
            -EX) AddressEntity
             */
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

    /* 정리
    -엔티티 타입의 특징
        =식별자 O
        =생명 주기 관리
        =공유
    -값 타입의 특징
        =식별자 X
        =생명 주기를 엔티티에 의존
        =공유하지 않는 것이 안전(복사해서 사용)
        =불변 객체로 만드는 것이 안전

    값 타입은 정말 값 타입이라 판단될 때만 사용
    엔티티와 값 타입을 혼동해서 엔티티를 값 타입으로 만들면 안됨
    식별자가 필요하고, 지속해서 값을 추적, 변경해야 한다면 그것은 값 타입이 아닌 엔티티
     */
