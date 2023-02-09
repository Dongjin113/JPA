package hellojpa.Section6VarietyAssociationMapping.VarietyAssociationMapping.ManyToOneMapping;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity
public class Team {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    //양방향으로 추가
    @OneToMany(mappedBy = "team")// mappedBy를 꼭 넣어줘야한다
    private List<Member> members = new ArrayList<>();

    /*다대일 양방향 정리
    -외래 키가 있는 쪽이 연관관계의 주인
    -양쪽을 서로 참조하도록 개발
 


*/
}
