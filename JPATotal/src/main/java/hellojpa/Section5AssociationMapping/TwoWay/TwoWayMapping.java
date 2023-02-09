package hellojpa.Section5AssociationMapping.TwoWay;

import org.hibernate.internal.build.AllowSysOut;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class TwoWayMapping {

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            TwoWayTeam team = new TwoWayTeam();
            team.setName("TeamA");
            em.persist(team);

            TwoWayMember member = new TwoWayMember();
            member.setTeam(team); //1.**이것과
            member.setUsername("member1");
            em.persist(member);

            //둘 중에 한군대에서만 해야한다 두개다 하게된다면 최악의 경우에 무한루프에 걸릴 수있고 고민거리등이 많아진다

            /* 1. getter와 setter내에 추가해서 사용 하는 방법
            //양쪽에다 값을 다 설정해준다
            team.getMembers().add(member); //2. ** 이것 둘을 같이 넣어야한다
            //연관관계 편의 메소드의 생성을 권장 --> TwoWayMember class내에 set에서 생성해준다 그리고 코드를지운다
            */

            team.addMember(member); //2) 함수를 생성해서 양쪽값에 대입하는방법


            //저장

/*          양방향 매핑시 가장 많이 하는 실수
            (연관관계의 주인에 값을 입력하지 않음)
            TwoWayMember member = new TwoWayMember();
            member.setUsername("member1");
            em.persist(member);

            TwoWayTeam team = new TwoWayTeam();
            team.setName("TeamA");
            //역방향(주인이 아닌 방향)만 연관관계 설정
            team.getMembers().add(member);
            em.persist(team);

            member의 TEAM_ID값이 null로들어간다
            */

            //역방향(주인이 아닌 방향)만 연관관계 설정
//          team.getMembers().add(member);


            em.flush();
            em.clear();

/*            System.out.println("멤버아이디"+member.getId());
            TwoWayMember findMember = em.find(TwoWayMember.class, member.getId());
            //양방향 연관관계 멤버에서 팀으로 팀에서 멤버로
            List<TwoWayMember> members = findMember.getTeam().getMembers();

            for (TwoWayMember m : members) {
                System.out.println("m = " + m.getUsername());
            }*/


            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();



    }
}

/*
    양방향 연관관계 주의 - 실습
    -순수 객체 상태를 고려해서 항상 양쪽에 값을 설정하자
    -연관관계 편의 메소드를 생성하자
    -양방향 매핑시에 무한 루프를 조심하자
        예: toString(), lombok, JSON 생성 라이브러리
*/

/*
    양방향 매핑 정리
        -단방향 매핑만으로도 이미 연관관계 매핑은 완료
        -양방향 매핑은 반대 방향으로 조회(객체 그래프 탐색)기능이 추가 된 것 뿐
        -JPQL에서 역방향으로 탐색할 일이 많음
        -단방향 매핑을 잘 하고 양방향은 필요할 때 추가해도 됨 (테이블에 영향을 주지 않음)
 */

/* 연관관계의 주인을 정하는 기준
-비즈니스 로직을 기준으로 연관관계의 주인을 선택하면 안됨
-연관관계의 주인은 외래 키의 위치를 기준으로 정해야 함
*/