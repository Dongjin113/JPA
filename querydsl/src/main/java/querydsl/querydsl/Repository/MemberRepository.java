package querydsl.querydsl.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import querydsl.querydsl.dto.MemberSearchCondition;
import querydsl.querydsl.dto.MemberTeamDto;
import querydsl.querydsl.entity.Member;

import java.awt.print.Pageable;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> , MemberRepositoryCustom, QuerydslPredicateExecutor {
    //select m from Member m where m.username
    List<Member> findByUsername(String username);


}
