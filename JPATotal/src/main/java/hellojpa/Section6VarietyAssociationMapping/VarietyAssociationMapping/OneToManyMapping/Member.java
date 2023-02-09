package hellojpa.Section6VarietyAssociationMapping.VarietyAssociationMapping.OneToManyMapping;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    //일대다 양방향 매핑을 거는것 까지는 똑같다 연관관계 주인처럼 되버리기 때문에 주인이 두개가 된다
    // Member, Team모두 주인이 된다 뭘로 들어갈지 예측불가
    //insertable , updatable false로 읽기전용으로 만들어준다 매핑은 되어있고 값은 다쓰나 최종적으로 insert나 update를 안하게해서
    //읽기전용으로 만든다 그래서 양방향매핑이 된것과 똑같이 사용할수 있게 된다
    /*    일대다 양방향
    - 이런 매핑은 공식적으로 존재 하지않는다
    - @JoinColumn(insertable=false, updateble= false)
    - 일기 전용 필드를 사용해서 양방향 처럼 사용하는 방법
    - 다대일 양방향을 사용하자*/
    @ManyToOne
    @JoinColumn(name = "TEAM_ID" ,insertable = false, updatable = false)
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
