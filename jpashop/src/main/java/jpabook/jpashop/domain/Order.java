package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    //검색은 Control shift F
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    //EAGER로 사용시 JPQL : select o From order o;라고 가져온다면 -> SQL : select * from order; n+1이 실행된다.
    // 결과가 100개라면 100+1번 실행된다
    //ManyToOne,OneToOne 은 Default가 Eager라서 전부다 Lazy로 변경해줘야하고 OneToMany는 default가 lazy이다

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    /* cascade : orderItemsA , orderItemsB, orderItemsC, order를 각각 persist를 사용해야하지만
        cascade를 사용할시에 order만 persist해도 oderItemA,B,C가 같이 persist된다
    */

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //    연관관계메서드 위치는 핵심적으로 뭔가 Control하는 쪽이 들고 있으면 좋다
    //양방향은 양쪽에 데이터를 추가해주어야한다 따라서 주를 이루는 Order에서 값이추가되면 member에도 동시에 추가될 수 있는 setter를 설정해준다
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //생성메서드//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /*
    * 주문 취소
    * */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /* 전체 주문 가격 조회*/

    public int getTotalPrice(){
        int totalprice = 0;
        for (OrderItem orderItem : orderItems) {
            totalprice += orderItem.getTotalPrice();
        }
        return  totalprice;
    }

    //Stream을 사용하여 한줄로 줄임
/*    public int getTotalPrice(){
        return  orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }*/

}
