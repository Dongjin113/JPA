package hellojpa.Section8ProxyAndAssociationMapping.EagerLazyLoading.CASCADE;


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
/*            Child child1 = new Child();
            Child child2 = new Child();
            Child child3 = new Child();


            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);
            em.persist(child1);
            em.persist(child2);
            em.persist(child3);*/

            Child child1 = new Child();
            Child child2 = new Child();
            Child child3 = new Child();


            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);

            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
            findParent.getChildList().remove(0);

            //Parent에서 @OneToMany(cascade = CascadeType.ALL)을 사용하면
            //em.persist 에서 parent만  persist 했는데 parent에 추가된 child persist
            // 두개또한 같이 persist되는 것을 볼 수 있다 parent에 추가되지 않은 child3은 추가되지 않는다



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
    CASCADE
    -특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속상태로 만들고 싶을 때
    - 예: 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장.

    !!영속성 전이 : CASCADE - 주의!!
    -영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음
    -엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공할 뿐

    하나의 부모자 자식들을 관리할 때(소유자가 하나일 때)
    ex) 게시판, 첨부파일의 경로 같은 경우에는 사용가능
    안되는 ex) 파일을 여러군대에서 관리를 할 때

    고아 객체
    - 고아 객체 제거: 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제
    - orphanRemoval = true
    - Parent parent1 = em.find(Parent.class, id);
      parent1.getChildren().remove(0);
      //자식 엔티티를 컬렉션에서 제거
    - DELETE FROM CHILD WHER ID=?

    고아 객체 -주의
    -참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아객체로 보고 삭제하는 기능
    -참조하는 곳이 하나일 때 사용해야함!
    -특정 엔티티가 개인 소유할 때 사용
    -@OneToOne, @OneToMany만 가능
    -참고: 개념적으로 부모를 제거하면 자식은 고아가 된다. 따라서 고아 객체 제거 기능을 활성화 하면
    , 부모를 제거할 때 자식도 함께 제거된다. 이것은 CasecadeType.REMOVE처럼 동작한다.

    영속성 전이 + 고아 객체, 생명주기
    -CascadeType.All + orphanRemovel=true
    -스스로 생명주기를 관리하는 엔티티는 em.persist()로 영속화, em.remove()로 제거
    -두 옵션을 모두 활성화 하면 부모 엔티티를 통해서 자식의 생명주기를 관리할 수 있음
    -도메인 주소 설계(DDD)의 Aggregate Root 개념을 구현할 때 유용

 */