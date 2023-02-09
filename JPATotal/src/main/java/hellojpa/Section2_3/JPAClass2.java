package hellojpa.Section2_3;

import hellojpa.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JPAClass2 {
    public static void main(String[] args){
        /*
        JPA에서 가장 중요한 2가지
        - 객체와 관계형 데이터 베이스 매핑하기 (Object Relational Mapping)
        - 영속성 컨텍스트


        영속성 컨텍스트란?
        -JPA를 이해하는데 가장 중요한 용어
        -"엔티티를 여구 저장하는 환경"이라는 뜻
        -EntityManager.persist(entity);
        */

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
/*            //비영속
            Member member = new Member();
            member.setId(101L);
            member.setName("HelloJPA");

            //영속
            System.out.println("===BEFORE===");
            //em.persist(member);
            System.out.println("===AFTER===");

            Member findMember1 = em.find(Member.class , 101L);
            Member findMember2 = em.find(Member.class , 101L);

            System.out.println("findMember.id=" + findMember1.getId());
            System.out.println("findMember.name=" + findMember1.getName());
            System.out.println("findMember.id=" + findMember2.getId());
            System.out.println("findMember.name=" + findMember2.getName());

            System.out.println("result = " + (findMember1 == findMember2));
            //1차 캐쉬로 반복가능한 읽기(REPEATABLE READ) 등급의 트랜잭션 격리 수준을
            //데이터베이스가 아닌 애플리케이션 차원에서 제공해준다*/

/*

                    ============수정 ,쓰기지연==============
            Member member1 = new Member(150L,"A");
            Member member2 = new Member(160L,"B");

            //이순간 데이터베이스에 저장되는것이 아니라 영속성컨텍스에 차곡차곡 Entity도 쌓이고 쿼리도 쌓이다
            //커밋을 하는시점에 데이터베이스로 날아간다
            em.persist(member1);
            em.persist(member2);
            System.out.println("====================");

            //영속 엔티티 조회
            Member member = em.find(Member.class, 150L);
            //영속 엔티티 데이터 수정
            member.setName("ZZZZZZ");


            //스냅샷이랑 값을 읽어온 최초의 시점을 스냅샷으로 떠둔것
            //스냅샷고 Entity를 비교후 값이 바뀌었을때 update쿼리를 쓰기지연 SQL저장소에 저장 하고 커밋시 데이터베이스에 반영
*/



            /*
            플러시
            영속성 컨텍스트의 변경내용을 데이터베이스에 반영

            플러시 발생
            -변경 감지
            - 수정된 엔티티 쓰기 지연 SQL 저장소에 등록
            -쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송 (등록, 수정, 삭제 쿼리)

            영속성 컨텍스트를 플러시하는 방법
            * em.flush() - 직접호출
            * 트랜잭션 커밋 - 플러시 자동 호출
            * JPQL 쿼리 실행 - 플러시 자동 호출

            em.persist(MemberA);
            em.persist(MemberB);
            em.persist(MemberC);

            //중간에 JPQL 실행
            query = em.createQuery("select m from Member m", Member.class);
            DB에서 데이터를 가져오는데 persist가 commit이 된상태가 아니라 영속성 컨텍스트에 1차로 저장된상태라
            DB에서 가져오는 데이터가 없는것을 방지하기위해 JPQL이 실행될때 JPA에서 기본으로 flush가 실행되게 설정되어있다.
            List<Member> members = query.getResultList();

            플러시 모드 옵션
            em.setFlushMode(FlushModeType.COMMIT)

                -FlushModeType.AUTO (손안대면 AUTO)
                 커밋이나 쿼리를 실행할 때 플러스(기본값)
                -FlushModeType.COMMIT
                 커밋할 떄만 플러시
                 
            플러시는!
            -영속성 컨텍스트를 비우지 않음
            -영속성 컨텍스트의 변경내용을 데이터베이스에 동기화
            -트랜잭션이라는 작업 단위가 중요 -> 커밋 직전에만 동기화 하면 됨



            Member member = new Member(200L,"member200");
            em.persist(member);
            em.flush(); //commit을 하는시점이 아닌 query가 DB에 반영이 된다
            System.out.println("========================");
            */

            /*
                     ====준영속 상태=====
            영속 -> 준영속
            영속 사태의 엔티티가 영속성 컨텍스트에서 분리(detached)
            영속성 컨텍스트가 제공하는 기능을 사용 못함

            준영속 상태로 만드는 방법
            -em.detach(entity)
            특정 엔티티만 준영속 상태로 전환
            -em.clear()
            영속성 컨텍스트를 완전히 초기화
            -em.close()
            영속성 컨텍스트를 종료
             */

            //영속
            Member member = em.find(Member.class, 150L);
            member.setName("AAAAA");

            //JPA에서 관리를 하지 않는 것
            //commit을 할때 아무일도 일어나지 않는다
            //select Query만 나오고 update Query는 안나온다
            //이것과 관련된 모든게 영속성컨텐트에서 빠진다
            //em.detach(member);

            //entityManager내의 모든 영속성컨텐트를 통으로 삭제
            em.clear();

            // 첫번째 쿼리가 삭제되서 다시 query가 실행이 되서 총 두번이 실행이 된다
            Member member2 = em.find(Member.class, 150L);

            System.out.println("==============");


            //insert SQL은 em.persist에서 보내지않고 commit을 하는 순간 보낸다
            tx.commit();

        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
            emf.close();
    }

}
