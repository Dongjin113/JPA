package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findHelloBy();

//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age")int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) " +
            "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionalByUsername(String username);

//count쿼리를 분리하는 방법
    @Query(value = "select m from Member m ", countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) //clear을 자동으로 해준다
    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
//  Modifying이 있어야 excuteUpdate를 실행한다 없을시 result를 실행한다
    int bulkAgePlus(@Param("age") int age);
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

//    Entitydp NameEntityGraph를 설정하여 사용할 수 있따
//    @EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member,all")
    List<Member> findEntityGraphByUsername(@Param("username") String username);


    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String name);

    List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);

     <T> List <T> findProjectionsClassByUsername(@Param("username") String username, Class<T> type);

     @Query(value = "select * from Member where username = ?", nativeQuery = true)
     Member findByNativeQuery(String username);

     @Query(value = "select m.member_id as id, m.username, t.name as teamName" +
             " from Member m left join team t",
             countQuery = "select count(*) from member",
             nativeQuery = true)
     Page<MemberProjection> findByNativeProjection(Pageable pageable);






}
