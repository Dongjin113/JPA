package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.Item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;//주문 가격
    private int count;//주문 수량

    //jpa는 protected까지 기본생성자를 만들수 있게 허용해준다
   /*
   @NoArgsConstructor(access = AccessLevel.PROTECTED)로 생략가능하다
   protected OrderItem() {
    }*/

    //생성 메서드//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //비즈니스 로직//
    public void cancel() {
        getItem().addStock(count);
    }

    //조회 로직//
    /* 주문상품 전체 가격 조회*/
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
