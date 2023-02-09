package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext //EntityManagerFactory를 자동으로 생성해준다 @RequiredArgsConstructor을 사용하면 생략가능하다
    private final EntityManager em;

    /*
    //엔티티 매니저 팩토리를 직접주입하고시다면
    @PersistenceUnit
    private EntityManagerFactory emf;
    */

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
       return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name",name)
                .getResultList();
    }

}
