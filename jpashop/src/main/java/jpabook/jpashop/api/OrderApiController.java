package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public Result orderV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {

            order.getMember().getName();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach( o -> o.getItem().getName());
        }
        return new Result(all);
    }

    @GetMapping("/api/v2/orders")
    public Result orderV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o)).collect(toList());

        return new Result(collect);
    }

    @GetMapping("/api/v3/orders")
    public Result orderV3(){
            List<Order> orders =orderRepository.findAllWithItem();
            List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o)).collect(toList());

        for (Order order : orders) {
            System.out.println("order ref=" + order + " id="+order.getId());
        }
            return new Result(collect);
    }

    @GetMapping("/api/v3.1/orders")
    public Result orderV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        //ToOne관계를 가지는 애들은 paging에 영향을 주지 않기때문에 한방에 가져오고
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        //그다음에 default batchSize로 Lazy발생시 데이터를 묶어서 가져온다
//        application.yml에서도 default로 설정할 수 있고
//        Entity에서 collection에 적용할 때는 collection위에 @BatchSize(size = 100) 이런식으로 적용할 수 있다
//        ToOne관계일 때는 class위에 적어서 사용할 수 있다
        List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o)).collect(toList());

        for (Order order : orders) {
            System.out.println("order ref=" + order + " id=" + order.getId());
        }
        return new Result(collect);
    }
//    ToOne 관계를 모두 페치조인한다. ToOne관계는 row수를 증가시키지 않으므로 페이징 쿼리에 영향을 주지 않는다

    @GetMapping("/api/v4/orders")
    public Result ordersV4(){
        List<OrderQueryDto> find = orderQueryRepository.findOrderQueryDtos();
        return new Result(find);
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6(){
//        OrderFlatDto를 OrderQueryDto로 사용하고 싶을때 중복을 직접걸러낸다
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        //메모리에서 코드를 돌려 OrderQueryDto와 OrderItemQueryDto를 map안에서 OrderQueryDto로 변경해줌
//        OrderQueryDto에서 @EqualsAndHashCode(of = "orderId")를 작성하면 orderId를 기준으로 하나로 묶어준다
        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(),
                                o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(),
                                o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
                        e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
                        e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
//      value Object는 사용가능하다 그외에는 전부 Dto로 호출해야한다
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(toList());
        }

//     프로퍼티가 없다는 뜻은 Getter Setter가 없다는 뜻
        @Data
        static class OrderItemDto{

            private String itemName;
            private int orderPrice;
            private int count;

            public OrderItemDto(OrderItem orderItem) {
                itemName = orderItem.getItem().getName();
                orderPrice = orderItem.getOrderPrice();
                count = orderItem.getCount();
            }
        }
    }
}
