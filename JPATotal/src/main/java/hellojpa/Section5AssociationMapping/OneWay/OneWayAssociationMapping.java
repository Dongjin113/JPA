package hellojpa.Section5AssociationMapping.OneWay;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class OneWayAssociationMapping {
    /*
    연관관계 매핑 기초
    객체와 테이블 연관관계의 차이를 이해
    객체의 참조와 테이블의 외래 키를 매핑
    용어 이해
        - 방향(Direction): 단방향, 양방향
        - 다중성(Multiplicity): 다대일(N:1), 일대다(1:N), 일대일(1:1), 다대다(N:M) 이해
        - 연관관계의 주인(Owner): 객체 양방향 연관관계는 관리 주인이 필요

    예제 시나리오
     회원과 팀이 있다
     회원은 하나의 팀에만 소속될 수 있다.
     회원과 팀은 다대일 관계다.

     */

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
/*          OneWayTeam team = new OneWayTeam();
            team.setName("TeamA");
            em.persist(team); //영속에 들어감

            oneWayMember member = new oneWayMember();
            member.setUsername("member1");
            member.setTeamId(team.getId());
            em.persist(member);

            //식별자로 다시 조회, 객체 지향적인 방법은 아니다.
            //조회
            oneWayMember findMember = em.find(oneWayMember.class, member.getId());
            Long findTeamId = findMember.getTeamId();
            //연관관계가없음
            OneWayTeam findTeam = em.find(OneWayTeam.class, findTeamId);*/
            /* 객체를 테이블에 맞추어 데이터 중심으로 모델링하면, 협력 관계를 만들 수 없다.
            -테이블은 외래 키로 조인을 사용해서 연관된 테이블을 찾는다.
            -객체는 참조를 사용해서 연관된 객체를 찾는다.
            -테이블과 객체 사이에는 이런 큰 간격이 있다.
            */

            //객체 지향적인 방법
            OneWayTeam team = new OneWayTeam();
            team.setName("TeamA");
            em.persist(team);

            OneWayMember member = new OneWayMember();
            member.setUsername("member1");
            member.setTeam(team); //그러면 JPA가 알아서 team에서 pk값을꺼내 insert할때 fk를 사용을 한다
            em.persist(member);

            em.flush();//영속성 컨텍스에 있는걸 db에 쿼리를 다날려버려서 싱크를맞추고
            em.clear();//영속성 컨텍스를 완전 초기화 시키는것

            OneWayMember findMember = em.find(OneWayMember.class, member.getId());
            OneWayTeam findTeam = findMember.getTeam();
            System.out.println("findTeam = "+ findTeam.getName());

/*            //수정 팀을바꾸고싶으면 다른 팀을찾고 setTeam에 값을 대입해주면 된다
            OneWayTeam newteam = em.find(OneWayTeam.class , 100L);
            findMember.setTeam(newteam);*/


            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();

    }
}
