package hellojpa.Section6VarietyAssociationMapping.VarietyAssociationMapping.ManyToManyMapping;

import hellojpa.Section6VarietyAssociationMapping.VarietyAssociationMapping.OneToOneMapping.Locker;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity
public class Member {

    @Id@GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    /*@ManyToMany
    @JoinTable(name = "MEMBER_PRODUCT")
    private List<Product> products = new ArrayList<>();*/

    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();


}
