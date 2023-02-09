package hellojpa.Section5AssociationMapping.OneWay;


import javax.persistence.*;

//@Entity
public class OneWayMember {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name="USERNAME")
    private  String username;

    /*@Column(name = "TEAM_ID")
    private Long teamId;
*/
    //다대일인지 일대다 인지 일대일인지 이 둘의 관계를 JPA에게 알려줘야한다 DB관점으로 중요
    //Member가 n이고 team이 1이다 하나의 팀에 여러개의 멤버가 소속되기 때문 그래서 member입장에서는 ManyToOne으로 맵핑해야한다
    //객체 연관관계인 OnewWayTeam team 레퍼런스와 테이블 연관관계인 FK인 TEAM_ID와 매핑해야한다
    @ManyToOne  //Member입장에서 many team으로는 1 one
    @JoinColumn(name = "TEAM_ID")
    private OneWayTeam team;

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

    public OneWayTeam getTeam() {
        return team;
    }

    public void setTeam(OneWayTeam team) {
        this.team = team;
    }
}
