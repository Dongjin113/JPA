package querydsl.querydsl.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import querydsl.querydsl.dto.MemberSearchCondition;
import querydsl.querydsl.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition condition);
    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable);
    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable);
}
