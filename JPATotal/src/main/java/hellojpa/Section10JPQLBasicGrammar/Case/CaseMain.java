package hellojpa.Section10JPQLBasicGrammar.Case;

import javax.persistence.*;
import java.util.List;

public class CaseMain {

    /* 조건식 - CASE식
    - COALESCE: 하나씩 조회해서 null 이 아니면 반환
    - NULLIF: 두 값이 같으면  null 반환, 다르면 첫번째 값 반환

    기본 CASE식
    select case when m.age <= 10 then '학생요금'
                when m.age >= 60 then '경로요금'
                else '일반요금'
            end
            from Member m

    단순 CASE식
    select case t.name
                when '팀A' then '인센티브 110%'
                when '팀B' then '인센티브 120%'
                else '인센티브105%'
                end
                from Team t

    *사용자 이름이 없으면 이름 없는 회원을 반환
    select coalesce(m.username, '이름 없는 회원') from Member m

    *사용자 이름이'관리자'면 null을 반환하고 나머지는 본인의 이름을 반환
    select NULLIF(m.username, '관리자') from Member m

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

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            //나이에따른 다른 요금을 반환
            String query1 = "select case when m.age <= 10 then '학생요금' " +
                    " when m.age >=60 then '경로요금' " +
                    " else '일반요금' end " +
                    " from Member m ";
            List<String> result1 = em.createQuery(query1, String.class).getResultList();

            for (String arg : result1) {
                System.out.println("s = " + arg);
            }

            //username이 없으면 이름없는 회원을 반환
            String query2 = "select coalesce (m.username, '이름 없는 회원') as username " +
                    " from Member m ";

            List<String> result2 = em.createQuery(query2, String.class).getResultList();
            for (String s : result2) {
                System.out.println("coalesce = "+ s);
            }

            //username이 관리자이면 null을반환
            String query3 = "select nullif (m.username, '관리자') as username " +
                    " from Member m ";

            List<String> result3 = em.createQuery(query3, String.class).getResultList();
            for (String s : result3) {
                System.out.println("coalesce = "+ s);
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
