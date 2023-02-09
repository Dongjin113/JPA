package hellojpa.Section7LuxuriousMapping.InheritanceRelationMapping.MappingSuperClass;

import hellojpa.Section6VarietyAssociationMapping.VarietyAssociationMapping.OneToManyMapping.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity
public class Team extends BaseEntity{

    @Id@GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;


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

}
