package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
* xTonOne(ManyToOne,OneToOne) 에서의 성능최적화를 어떻게 할 것인가?
* Order
* Order -> Member 연관 ManyToOne
* Order -> Delivery 연관 OneToOne
* */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepositoryRepository;

    //    1. 첫번째 문제 양방향 매핑중 한쪽방향은     @JsonIgnore로 끊어주어야 무한루프가 돌지 않는다
//    hibernate Module을 사용 JpashopApplication
//    api 응답으로 외부에 노출하는것은 좋지 않다
//    따라서 hibernate5Module을 사용하기보다는 DTO로 변환해서 반환하는 것이 더 좋은 방법이다
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        //order에 member를 조인해서 반환
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제초기화
            order.getDelivery().getAddress(); //Lazy강제초기화
        }
        return all;
    }

    //DTO로 반환하는 List
    @GetMapping("/api/v2/simple-orders")
    public Result orderV2(){
//  ORDER 2개
//        N + 1 -> 1 + 회원 N (2) + 배송 N (2)  총 5번 실행
//        성능이 좋지 않다 나중에 fetch join으로 성능개선
//        총5번의 쿼리가 발생
    List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                //.map(SimpleOrderDto::new)
                .collect(Collectors.toList());

        return new Result(result);
    }

    //fetchJoin 으로 최적화를한 Version3
//  쿼리가 한번 발생
//    많은곳에서 활용가능
    @GetMapping("/api/v3/simple-orders")
    public Result orderV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return new Result(result);
    }
    
//    JPA에서 바로 DTO로 변환 v3보다 조금 더 성능최적화가 가능
//    query가 원하는 것만 select해옴
//    재사용성이 안됌
//    Repository는 가급적이면 순수한 엔티티를 조회할 때 사용하기 떄문에 pakage를 하나 더 만들어서 사용해준다
@GetMapping("/api/v4/simple-orders")
    public Result orderV4(){



        return new Result(orderSimpleQueryRepositoryRepository.findOrderDtos());
    }



    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

}
