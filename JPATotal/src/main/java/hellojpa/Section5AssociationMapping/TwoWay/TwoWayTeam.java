package hellojpa.Section5AssociationMapping.TwoWay;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity
public class TwoWayTeam {

    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team") //Team 에서 Member로 가는것은 1팀에서 여러개의 멤버로가니까 1대 다 mappedBy는 반대편사이클의 맵핑되는 변수의이름
    private List<TwoWayMember> members = new ArrayList<>();
    //오너가 아니기 때문에 조회는가능하나 값을 수정할 때는 오너인 Member의 Team만 가능하다
    /*
    //mappedBy = JPA의 멘탈붕괴 난이도
    //mappedBy는 처음에는 이해하기 어렵다.
    //객체와 테이블간에 연관관계를 맺는 차이를 이해해야 한다.

    객체 연관관계 = 2개
        - 회원 -> 팀 연관관계 1개(단방향)
        - 팀 -> 회원 연관관계 1개(단방향)
    테이블 연관관계 = 1개
        - 회원 <-> 팀의 연관관계 1개(양방향)

    객체의 양방향 관계
    -객체의 양방향 관계는 사실 양방향 관계가 아니라 서로 다른 단방향 관계 2개다.
    -객체를 양방향으로 참조하려면 단방향 연관관계를 2개 만들어야 한다
    -A -> B(a.getB())  class A{B b;}
    -B -> A(b.getA())  class B{A a;}

    테이블의 양방향 연관관계
    - 테이블은 외래 키 하나로 두 테이블의 연관관계를 관리
    - MEBER.TEAM_ID 외래 키 하나로 양방향 연관관계 가짐 (양쪽으로 조인할 수 있다.)

    연관관계의 주인(Owner)
    양방향 매핑 규칙
        -객체의 두 관계중 하나를 연관관계의 주인으로 지정
        -연관관계의 주인만이 외래 키를 관리 (등록, 수정)
        -주인이 아닌쪽은 읽기만 가능
        -주인은 mappedBy 속성 사용X
        -주인이 아니면 mappedBy 속성으로 주인 지정
        
    누구를 주인으로?
    -외래 키가 있는 곳을 주인으로 정해라
    -여기서는 Member.team이 연관관계의 주인
    */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TwoWayMember> getMembers() {
        return members;
    }

    public void setMembers(List<TwoWayMember> members) {
        this.members = members;
    }

    public void addMember(TwoWayMember member) {
        member.setTeam(this);
        members.add(member);
    }

    /*
    컨트롤러 에서 엔티티를 json으로 api에 반환할시에 나중에 api스펙 자체가 변경될 수 있기때문에
    controller에서 엔티티를 사용할시에 dto로 변환을 해서 반환하는 것을 추천한다
    !! 컨트롤에서 엔티티를 반환하지 말기!!
     */

    @Override
    public String toString() {
        return "TwoWayTeam{" +
                "id=" + id +
                ", name='" + name + '\'' +
 //               ", members=" + members + //List이기 때문에 양쪽에서 toString 이 실행된다면 무한리프에 빠지게 된다 가능하면 쓰지말던가 List같은것은 빼고 사용해야한다
                '}';
    }
}
