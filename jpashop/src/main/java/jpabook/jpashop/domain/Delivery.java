package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;
    // 1:1 관계에서는 FK를 어디에 두어도 상관없다.
    // 각각의 장단점이 있는데
    // 강의에서는 주로 액세스하는 엔티티에 둔다고 한다.
    // 주로 오더에서 딜리버리를 조회할 일이 많기 때문에 오더에 FK를 두었다.

    // 여기서 연관관계도 정의할 때
    // Order가 FK를 가져갔기 때문에 Order를 주인으로 고려한다.

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // READY, COMP

    // Note. enum형 주의점
    // enum타입 사용시 @Enumerated와 함께 사용한다.
    // ORDINAL으로 타입 지정시 enum에서 사용하는 요소들이 숫자로 관리됨
    // 이때 READY, COMP 사이에 다른 요소가 추가된다면....
    // 순서가 뒤죽박죽이 되어 DB에서 조회할때 엉뚱한 요소가 나올 가능성이 매우 높음!!
    // 따라서 ORDINAL이 아닌 STRING으로 사용하자!!
}
