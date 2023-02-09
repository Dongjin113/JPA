package hellojpa.Section8ProxyAndAssociationMapping.Proxy;


import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ProxyMain {

    public static void main(String[] args){


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            /*Member member = em.find(Member.class, 1L);
            printMember(member);
            printMemberAndTeam(member);*/

            Member member1 = new Member();
            member1.setName("hello1");
            em.persist(member1);

//            Member member2 = new Member();
//            member2.setName("hello2");
//            em.persist(member2);

            em.flush();
            em.clear(); //영속성 초기화

//          Member findMember = em.find(Member.class, member.getId());

//            //getReference 를 호출하는 시점에는 쿼리를 생성을 하지 않으나 이 값을 실제 사용하는시점에 쿼리를 생성한다
//            Member findMember = em.getReference(Member.class, member1.getId());
//
//            System.out.println("findMember = "+ findMember.getClass());
//            System.out.println("findMember.id = "+ findMember.getId());
//            System.out.println("findMember.username = "+ findMember.getName());

            
            //*타입비교를 할때에는 절대 == 이아닌 instance of를 사용해야 한다
//            Member m1 = em.find(Member.class, member1.getId());
//            Member m2 = em.find(Member.class, member2.getId());
//            System.out.println("m1 == m2" + (m1.getClass() == m2.getClass()));
//
//            Member m3 = em.find(Member.class, member1.getId());
//            Member m4 = em.getReference(Member.class, member2.getId());
//            System.out.println("m3 == m4" + (m3.getClass() == m4.getId()));
//
//            logic(m1, m2);

            // 프록시로 한번 조회가 되면 em.find에서도 프록시로 반환을 해버린다 그래야 == 비교를 맞출수있기 떄문에
            Member refMember = em.getReference(Member.class , member1.getId());
            System.out.println("refMember = "+ refMember.getClass());

            //프록시 인스턴스의 초기화 여부확인
            //refMember.getName();
            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));

            //프록시 강제 초기화
            Hibernate.initialize(refMember);



            //em.detach(refMember);
            //em.close();

            //em.detach나 close clear를 사용하고 나면 영속성 콘텍스의 도움을 받을수 없기 떄문에 오류가 발생하게 된다
            //refMember.getName();

//            Member findMember = em.find(Member.class, member1.getId());
//            System.out.println("findMember =" + findMember.getClass());
//
//            System.out.println("refMember == findMember" + (refMember == findMember));

            tx.commit();
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
        emf.close();
    }

    private static void logic(Member m1, Member m2) {
        System.out.println("m1 == m2"+ (m1 instanceof  Member));
        System.out.println("m1 == m2"+ (m2 instanceof  Member));
    }

    private static void printMemberAndTeam(Member member) {
        String username = member.getName();
        System.out.println("username = "+ username);

        Team team = member.getTeam();
        System.out.println("team = "+ team.getName());
    }

    private static void printMember(Member member) {
        System.out.println("Member = " + member.getName());
    }
}

/* 프록시 기초
    -em.find() vs em.getReference()
    -em.find() : 데이터베이스를 통해서 실제 엔티티 객체 조회
    -em.getReference(): 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회
                        DB에 쿼리가 안나가는데 객체가 조회가됌

    프록시 특징
    -실제 클래스를 상속 받아서 만들어짐
    -실제 클래스와 겉 모양이 같다
    -사용하는 입장에서는 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면 됨(이론상)

    -프록시 객체는 실제 객체의 참조(targe)를 보관
    -프록시 객체를 호출하면 프록시 객체는 실제 객체의 메소드 호출

    프록시 객체의 초기화
    프록시 객체를 가져온 후 member.getName을 호출하면 멤버 타겟에 처음 값이 없으면 영속성 컨텍스트에 요청을 한다
    이후 영속성 컨텍스트가 DB조회후 실제 Entity객체에 준후 proxy의 target에 연결을 시켜준다

    !!! 중요
    -프록시 객체는 처음 상요할 때 한 번만 초기화
    -프록시 객체를 초기화 할 때, 프록시 객체가 실제 엔티티로 바뀌는 것은 아님,
     초기화 되면 프록시 객체를 통해서 실제 엔티티에 접근 가능
    -프록시 객체는 원본 엔티티를 상속받음, 따라서 타입 체크시 주의해야함(==비교 실패, 대신 instance of 사용)
    -영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 엔티티 반환
    -영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화할때 문제 발생
    (하이버네이트는 org.gibernate.LzyInitializationException 예외를 터트린다)


    프록시 확인
    -프록시 인스턴스의 초기화 여부 확인
     PersistenceUntilUtil.isLoaded(Object entity)
    -프록시 클래스 확인 방법
    -프록시 강제 초기화
    -참고 : JPA 표준은 강제 초기화 없음
 */