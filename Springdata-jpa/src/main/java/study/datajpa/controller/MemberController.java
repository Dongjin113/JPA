package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }


// /members?page=2&size=3&sort=id,desc&sort=username,desc 기본이 asc
//  default= 20 default를 바꾸고 싶다면 global 설정은 yml파일에서 설정을 하면 된다

//  이곳에만 특별히 설정하고 싶다면 PageableDefault를 설정해주면 된다
    @GetMapping("/members1")
    public Page<Member> list(@PageableDefault(size = 5, sort = "username") Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

//  page를 DTO로 반환할때
    @GetMapping("/members2")
    public Page<MemberDto> listDto(@PageableDefault(size = 5, sort = "username") Pageable pageable){
//        페이지를 1부터 시작하고 싶을때
//        PageRequest request =PageRequest.of(1,2);


        Page<Member> page = memberRepository.findAll(pageable);
//        control alt n 을 사용하면 자동으로 코드를 합쳐준다
//        Page<MemberDto> map = page.map(member -> new MemberDto(member));
        Page<MemberDto> map = page.map(MemberDto::new);
        return map;
    }

//    @PostConstruct
    public void init(){
//        members/id
//        memberRepository.save(new Member("userA"));

        for(int i = 0; i<100; i++){
            memberRepository.save(new Member("user"+i,i));
        }

    }


}
