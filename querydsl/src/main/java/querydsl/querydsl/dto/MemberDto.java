package querydsl.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MemberDto {

    private String username;
    private int age;

    public MemberDto() {
    }

    @QueryProjection // gradle compileQuerydsl을 실행하면 Dto도 Q파일로 생성되게해준다
    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
