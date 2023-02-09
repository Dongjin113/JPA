package jpabook.jpashop.Service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /* 주문 */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        //엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 도메인 모델 패턴이라고 한다

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        /*OrderItem orderItem1 = new OrderItem();
        의 형태로 사용할 수 없도록 OrderItem에 protected
        생성자를 만들어 사용을 막고
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);의 형태로 사용을 강요한다
         사용방식을 통일해줌으로써 추후 유지보수를 쉽게하기 위함
        */

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //보통 cascade 사용범위 order delivery관리  order oderitem관리 주인이
        // private owner인 경우에만 사용해야한다
        //delivery, orderitem order만 참조해서 사용한다 orderitem이
        // 다른곳을 참조하는 경우는 있지만 다른곳에서 orderitem을 참조하는 곳은 없다
        orderRepository.save(order);

        return order.getId();

    }

    /*주문 취소*/
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 entity조회
        Order order = orderRepository.findOne(orderId);
        //주문취소
        order.cancel();
    }

    //검색
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByString(orderSearch);
    }

}
