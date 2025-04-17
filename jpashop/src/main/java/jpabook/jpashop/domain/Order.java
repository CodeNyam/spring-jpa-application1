package jpabook.jpashop.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // Q. FK 값을 변경하고 싶다면, Member, Order 엔티티 중에서 무엇을 바꾸어야 할까?
    // 현재 Member에는 orders, Order에는 Member 필드가 존재
    // JPA에서 둘 다 값을 확인하여 같이 바꾸어야 할까?
    // 예시로 Order의 Member에는 값을 입력했으나, Member 에는 입력하지 않음...

    // JPA는 둘 중 하나만, 주인으로 채택하여 변경하기를 규칙으로 정함
    // 즉, 연관관계 주입을 통해 주인과 하인 관계를 정함
    // 주인은 FK가 가까운 엔티티가 된다.

    // 따라서 이 상황에서 Order - Member 관계에서 Order가 주인이 된다.


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItems> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_name")
    private Delivery delivery;

    private LocalDateTime orderDate;

    private OrderStatus status;

    // ==연관관계 메서드== //
    // 보통 비즈니스 로직에서 String[] args를 통해 엔티티를 주입해줄 수 있지만, 까먹을수도 있으니
    // 메서드와 로직을 묶어준다.
    // 이 메서드를 통해 new로 엔티티를 생성해주지 않고, 로직을 수행할 수 있다.
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItems orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

}
