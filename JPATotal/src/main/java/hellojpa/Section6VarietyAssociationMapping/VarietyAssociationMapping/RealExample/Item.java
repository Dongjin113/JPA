package hellojpa.Section6VarietyAssociationMapping.VarietyAssociationMapping.RealExample;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity
public class Item {

    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;

    private int price;

    private int stockQuanity;

    @ManyToMany
    private List<Category> categories = new ArrayList<>();


}
