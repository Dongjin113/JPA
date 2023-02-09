package hellojpa.Section7LuxuriousMapping.InheritanceRelationMapping.MappingSuperClass;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class MappingSuperClassMain {

    public static void main(String[] args){
        EntityManagerFactory emf= Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            Member member = new Member();
            member.setCreatedBy("kim");
            member.setCreatedDate(LocalDateTime.now());

            em.persist(member);
            em.flush();
            em.clear();

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }

    /* @MappedSuperclass
        -상속관계 매핑X
        -엔티티X, 테이블과 매핑X
        -부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공
        -조회, 검색 불가(em.find(BaseEntitiy)불가)
        -직접 생성해서 사용할 일이 없으므로 추상 클래스 권장
        -테이블과 관계 없고, 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할
        -주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용
        -참고: @Entitiy 클래스는 엔티티나 @MappedSuperclass로 지정한 클래스만 상속 가능
            =@Entity가있거나 @MappedSuperclass가 있어야지만 상속이 가능하다
                클래스명위에 어노테이션이 있어야한다



     */


}
