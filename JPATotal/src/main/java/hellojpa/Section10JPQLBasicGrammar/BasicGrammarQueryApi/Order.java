package hellojpa.Section10JPQLBasicGrammar.BasicGrammarQueryApi;

import javax.persistence.*;

//@Entity
@Table(name = "ORDERS")
//Order는 예약어라 오류가날 수 있어 ORDERS라고 많이사용한다
public class Order {

    @Id @GeneratedValue
    private Long id;
    private int orderAmount;

    @Embedded
    private Address adress;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Address getAdress() {
        return adress;
    }

    public void setAdress(Address adress) {
        this.adress = adress;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
