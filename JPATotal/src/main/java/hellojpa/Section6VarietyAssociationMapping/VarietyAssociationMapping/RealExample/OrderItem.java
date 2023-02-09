package hellojpa.Section6VarietyAssociationMapping.VarietyAssociationMapping.RealExample;

import javax.persistence.*;

//@Entity
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "ORDERITEM_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    private int orderPrice;

    private int count;

}
