package hellojpa.Section10JPQLBasicGrammar.Select;

import javax.persistence.*;
import java.util.List;


public class SelectMain {
    /* 프로젝션
    - Select 절에 조회할 대상을 지정하는 것
    - 프로젝션 대상 : 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자 등 기본 데이터 타입)
    - SELECT m FROM Member m -> 엔티티 프로젝션
    - SELECT m.team FROM Member m -> 에닡티 프로젝션
    - SELECT m.address FROM Member m -> 임베디드 타입 프로젝션
    - SELECT m.username, m.age FROM Member m -> 스칼라타입 프로젝션
    - DISTINCT로 중복제거 */

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx= em.getTransaction();
        tx.begin();
        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            //m이 entity라 List<Member> 또한 엔티티들이 반환이 된다
            //영속 컨텍스트에서 관리가 됌
            //엔티티타입 프로젝션
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();
            //업데이트 쿼리문이 나간다
            Member findMember = result.get(0);
            findMember.setAge(20);

            //join 된 쿼리가 나가지면 SQL와 동일하게 사용하는것이 좋다
            List<Team> resultTeam1 = em.createQuery("select m.team from Member m", Team.class)
                    .getResultList();

            //권장하는 방법 -->
            List<Team> resultTeam2 = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();

            //임베디드 타입 프로젝션 Order entity안에 있는 address만을 읽어옴
            //select Address from  Address 라고 사용할수 없다  임베디드이기 떄문에 어디소속인지를 정해주어야 한다
            em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();

            //스칼라 타입 프로젝션
            em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();

            /* 프로젝션 - 여러 값 조회
            - SELECT m.username, m.age FROM Member m
                = 1. Query 타입으로 조회
                = 2. Object[] 타입으로 조회
                = 3. new 명령어로 조회
                    - 단순 값을 DTO로 바로 조회
                        SELECT new jpabook.jpql.UserDTO(m.username, m.age) FROM Member m
                    - 패키지 명을 포함한 전체 클래스 명 입력
                    - 순서와 타입이 일치하는 생성자 필요
           */
            //스칼라타입 타입이 여러개라 타입명시가 힘들어
            //1. Object 타입으로 조회
            List resultScala1 = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();
            Object o = resultScala1.get(0);
            Object [] ObjResult1 = (Object[])  o;
            System.out.println("username =" + ObjResult1[0]);
            System.out.println("age =" + ObjResult1[1]);

            //2. TypeQuery 사용하기
            List<Object[]> resultScala2 = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();

            Object[] ObjResult2 = resultScala2.get(0);
            System.out.println("username2 = "+ ObjResult2[0]);
            System.out.println("age2 = "+ ObjResult2[1]);
            
            //3. 가장 깔끔한 new 명령어로 조회
            List<MemberDTO> resultScala3 = em.createQuery("select new hellojpa.Section10JPQLBasicGrammar.Select.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();
            MemberDTO DTOresult = resultScala3.get(0);
            System.out.println("memberDTO = "+ DTOresult.getUsername());
            System.out.println("memberDTO = "+ DTOresult.getAge());

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
