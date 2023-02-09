package jpabook.jpashop.api;

import jpabook.jpashop.Service.MemberService;
import jpabook.jpashop.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.transform.Result;
import java.util.List;
import java.util.stream.Collectors;

//@Controller  @ResponseBody 두개가 합쳐져서
@RestController //의 형태를 사용한다
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /*조회하기*/

//  엔티티를 직접노출하게되면 엔티티에있는 모든 정보들이 노출이된다
//  Entity에 JsonIgnore을 사용하면 ignore한 정보가 빠지게된다
//  그러나 Entity에 ignore을 사용하면 entity를 사용하는 파일이 많아질수록 사용하는 곳이 
//  있고 사용하지 않는 곳이 생길 수 있기 때문에 좋은 방법이 아니다
//  json형태의 array를 바로 반환하기 때문에 data를 확장하기 힘들다 그래서 result타입으로 한번 감싸줘야한다
    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }

    //추천하는 방법
    //껍대기가 생기고 data[]배열로 데이터를 감싸게된다
    //그리고 name만 보이게 된다
    @GetMapping("/api/v2/members")
    public Result memberV2(){
        List<Member> findMembers = memberService.findMembers();
        //List<Member>를 List<MemberDTO>로 변경하는 것
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        //보이고 싶은 것만 추가
        private String name;
    }


    /*등록하기*/

    @PostMapping("/api/v1/members")
    //RequestBody Json으로 온 body를 Member에 그대로 Mapping해서 넣어준다
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

//  추천하는 방법 등록하기
    //Entity를 parameter로 직접 받는 v1은 좋은방식이 아니다 v2처럼 DTO를 사용하는 것이 추천되는 방법이다
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
    @Data
    static class CreateMemberRequest {
        private String name;
    }


    /*수정하기*/


    //update용 requestDTO와 update용 응답 DTO를 별도로 만들어준다
    //등록이랑 수정은 API스펙이 다 달라서 별도의 DTO를 가지는 것이 좋다
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id, request.getName());

//      커맨드와 쿼리를 분리하기위해 findOne으로 쿼리를 호출
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());

    }
    @Data
    @AllArgsConstructor
    private class UpdateMemberResponse {
        private Long id;
        private String name;

    }
    @Data
    private class UpdateMemberRequest {
        private  String name;

    }
}
