package querydsl.querydsl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import querydsl.querydsl.entity.Member;
import querydsl.querydsl.entity.Team;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMember {

    private final InitMemberService initMemberService;
//PostConstruct와 Transactional을 같이 사용되는것이 안되기 때문에 분리해줘야한다

    @PostConstruct
    public void init(){
        initMemberService.init();
    }

    @Component
    static class InitMemberService{

        @PersistenceContext
        private EntityManager em;

//       스프링띄울때 자동으로 데이터 넣어놓고 api에서 조회용 api만 호출할려고 샘플데이터를 넣는것
        @Transactional
        public void init(){
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            em.persist(teamA);
            em.persist(teamB);

            for (int i = 0; i< 100; i++){
                Team selectedTeam = i%2 ==0? teamA : teamB;
                em.persist(new Member("member"+i, i, selectedTeam));
            }
        }

    }
}
