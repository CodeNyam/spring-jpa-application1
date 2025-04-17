package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    // PK 이름 지정
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded // 내장 타입 사용
    private Address address;

    @OneToMany(mappedBy = "member") // 연관관계에서 하인이 됨
    // Order 필드의 member와 매핑되어 행동
    private List<Order> orders = new ArrayList<>();
}
