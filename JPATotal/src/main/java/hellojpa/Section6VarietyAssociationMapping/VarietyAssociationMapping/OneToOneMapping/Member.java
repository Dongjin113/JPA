package hellojpa.Section6VarietyAssociationMapping.VarietyAssociationMapping.OneToOneMapping;

import javax.persistence.*;

//@Entity
public class Member {

    @Id@GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;



}
