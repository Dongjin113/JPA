package hellojpa.Section6VarietyAssociationMapping.VarietyAssociationMapping.ManyToOneMapping;

import javax.persistence.*;

//@Entity
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Column(name = "username")
    private String name;

}
