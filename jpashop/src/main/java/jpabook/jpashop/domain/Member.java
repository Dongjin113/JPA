package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    //entity에 바로 NotEmpty를 사용하는것은 api스펙에 변경을 줘 오류가 발생할 수 있기 때문에 좋은 방법이 아니다
    //DTO를 사용하고 DTO에 NotEmpty를 사용하는것을 권장한다
//    @NotEmpty
    private String name;

    @Embedded
    private Address address;

//양방향연관관계 매핑에서 한쪽을     @JsonIgnore 해줘야 무한루프가 돌지 않는다
    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();


}
