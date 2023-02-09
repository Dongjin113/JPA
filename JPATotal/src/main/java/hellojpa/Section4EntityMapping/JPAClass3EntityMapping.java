package hellojpa.Section4EntityMapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JPAClass3EntityMapping {

    /*
    엔티티 매핑
    -객체와 테이블 매핑 : @Entity, @Table
    -필드와 컬럼 매핑 : @Column
    -기본키 매핑 : @Id
    -연관관계 매핑: @ManyToOne, @JoinColumn

    =@Entity=
    @Entity가 붙은 클래스는 JPA가 관리, 엔티티라 한다.
    JPA를 사용해서 테이블과 매핑할 클래스는 @Entity필수

    주의
    기본생성자 필수(파라미터가 없는 public 또는 protected 생성자)
    final 클래스, enum, interface, inner 클래스 사용X
    저장할 필드에 final 사용 X

    속성: name
    JPA에서 사용할 엔티티 이름을 지정한다.
    기본값: 클래스 이름을 그대로 사용(예: Member)
    같은 클래스 이름이 없으면 가급적 기본값을 사용한다.

    @Entity(name= "Member")같은패키지에 같은이름의 class가있고 JPA로 맵핑이 돼있는 사용
    @Table(name = "MBR") insert into MBR로 나간다 Table은 엔티티와 매핑할 테이블 지정


    데이터베이스 스키마 자동 생성
    -DDL을 애플리케이션 실행 시점에 자동 생성
    -테이블 중심 - > 객체 중심
    -데이터베이스 방언을 활용해서 데이터베이스에 맞는 적절한 DDL 생성
    -이렇게 생성된 DDL은 개발 장비에서만 사용
    -생성된 DDL은 운영서버에서는 사용하지 않거나, 적절히 다듬은 후 사용


    DDL생성기능
    -제약조건 추가: 회원 이름은 필수, 10자 초과X
        -@Column(nullable = false, length =10)
    -유니크 제약조건 추가
        -@Table(uniqueConstraints={@UniqueConstraint(name = "NAME_AGE_UNIQUE", columsNames = {"NAME","AGE"})})
    - DDL 생성 기능은 DDL을 자동 생성할 때만 사용되고 JPA의 실행 로직에는 영향을 주지 않는다.


     요구사항 추가
     1. 회원은 일반 회원과 관리자로 구분해야 한다.
     2. 회원 가입일과 수정일이 있어야 한다.
     3. 회원을 설명할 수 있는 필드가 있어야 한다. 이 필드는 길이 제한이 없다
     */
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em =emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            Member member = new Member();
            member.setId(1L);
            member.setUsername("A");
            member.setRoleType(RoleType.GUEST); //RoleType이 0번으로 들어가있다 0,1,2,3

            em.persist(member);
            tx.commit();

        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();

    }
}
