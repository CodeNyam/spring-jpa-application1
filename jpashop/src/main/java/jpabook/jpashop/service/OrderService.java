package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // 주문상품 생성
        OrderItems orderItem = OrderItems.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        // order 엔티티에 Cascade가 적용되어 모든 order가 persist화
        // delivery도 마찬가지
        orderRepository.save(order);
        return order.getId();

        // Note. Cascade, 어디까지 적용해야할까?
        // 연관관계에서 오너가 프라이빗으로 선언될때 사용하면 좋다.
        // 반대로, 다른 엔티티에서도 참조하는 경우, Cascade를 남발하면 안된다.

        // 여기서, Delivery는 Order만 참조하고, OrderItems만 Order를 참조하기에 Cascade를 사용했다
        // 사실 Cascade를 언제 사용할지 감이 안잡힌다면 그냥 사용하지 말고
        // 깨달았을때 리팩토링하며 사용하는게 낫다.
    }


    // 취소
    @Transactional
    public void cancelOrder(Long orderId) {

        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        // 주문 취소
        order.cancel();
    }


    // 검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }
}

//Note. JPA의 큰 강점
//업데이트 쿼리를 날리지 않아도 된다!
//JPA는 자동으로 더티체킹을 지원해서, 엔티티(데이터)가 변경될 경우 자동으로 업데이트 해준다.