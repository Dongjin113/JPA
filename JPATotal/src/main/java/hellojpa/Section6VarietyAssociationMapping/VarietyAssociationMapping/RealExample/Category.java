package hellojpa.Section6VarietyAssociationMapping.VarietyAssociationMapping.RealExample;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity
public class Category {
    @Id @GeneratedValue
    private Long id;

    private String name;


    private List<Item> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    //카테고리가 쭈우우욱 내려가기때문에 셀프로하게끔 되게해준다
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();



}
