package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED) -> 생성자를 protected 레벨에서 선언
public class OrderItems {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격 -> 시간마다 바뀔수도 있음
    private int count; // 주문 수량

    // 다른 계층에서 비즈니스 로직을 수행하기 위해
    // new로 객체를 생성하는 것을 방지 -> 컴파일 에러
    // 오직, Order 엔티티만 비즈니스 로직을 수행
    protected OrderItems() {}

    // == 생성 메서드== //
    public static OrderItems createOrderItem(Item item, int orderPrice, int count) {
        OrderItems orderItem = new OrderItems();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    // ==비즈니스 로직== //
    /**
     * 주문 취소
     */
    public void cancel() {
        getItem().addStock(count);
    }

    // ==조회 로직== //
    /** 주문 상품 전체 조회
     *
     * @return
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
