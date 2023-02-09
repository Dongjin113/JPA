package hellojpa.Section10JPQLBasicGrammar.JPQLBasicFunction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JPQLMain {
    /* JPQL 기본함수
    - CONCAT : 문자를 더하는것
    - SUBSTRING : 문자를 N번부터 잘라내는 것
    - TRIM :공백문자를 제거하는 것
    - LOWER, UPPER : 대소문자로 변경하는것
    - LENGTH : 문자의 길이
    - LOCATE : 문자의 위치를 찾는 함수
    - ABS, SQRT, MOD :
    - SIZE, INDEX(JPA 용도) :

    사용자 정의 함수 호출
    -하이버네이트는 사용전 방언에 추가해야 한다
        =사용하는 DB 방언을 상속받고, 사용자 정의 함수를 등록한다.
    select function (' group_concat ', i.name ) from Item i
    */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx= em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);


            Member member = new Member();
            member.setUsername("관리자");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            Member member2 = new Member();
            member2.setUsername("관리자");
            member2.setAge(10);
            member2.setType(MemberType.ADMIN);

            member.setTeam(team);
            member2.setTeam(team);

            em.persist(member);
            em.persist(member2);

            em.flush();
            em.clear();

            //문자열 a와 b를 합침
            String query1 = " select 'a' || 'b' From Member m ";
            //2번째 부터 3개의 문자열 출력
            String substring = " select substring(m.username, 2, 3) From Member m ";
            //문자열에서 de를 찾아달라는 함수 결과 4를 출력
            String locate = " select locate('de','abcdeqf') From Member m ";
            //size collection의 크기를 찾는함수
            String size = " select size(t.member) From Team t ";
            //사용자 정의함수
            //dialect 폴더를 생성하고 H2Dialect를 상속받아 사용 사용방법은 H2Dialect를 참조하여 사용한다
            //persistence의 설정을 바꿔준다
            //데이터가 두줄이 나와야 하지만 group_concat 함수때문에 한줄로 나오게 된다
            String define1 = " select function('group_concat', m.username) From Member m ";
            //하이버네이트를 사용한다면 이런식으로 사용할 수 있다
            String define2 = " select group_concat(m.username) From Member m ";

            
            //index 사용을 추천하지 않음

            List<String> result = em.createQuery(define2, String.class).getResultList();

            for (String arg : result) {
                System.out.println("s = " + arg);
            }


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
