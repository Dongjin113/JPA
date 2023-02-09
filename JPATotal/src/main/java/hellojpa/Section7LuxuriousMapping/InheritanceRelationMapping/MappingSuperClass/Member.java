package hellojpa.Section7LuxuriousMapping.InheritanceRelationMapping.MappingSuperClass;

import javax.persistence.*;
import java.time.LocalDateTime;

//@Entity
public class Member extends BaseEntity{

    @Id@GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;


}
