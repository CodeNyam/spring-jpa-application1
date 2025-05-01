package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.NotEnoughStockException;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void orderItem() throws Exception {
        // Given
        Member member = createMember();
        entityManager.persist(member);

        Item item = createBook("JPA", 10000, 10);

        int orderCount = 2;

        // When
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);


        // Then
        Order getOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        Assertions.assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수는 정확해야 한다.");
        Assertions.assertEquals(10000*2, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        Assertions.assertEquals(8, item.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");

    }


    @Test
    public void cancelOrder() throws Exception{
        // Given
        Member member = createMember();
        Item item = createBook("JPA", 10000, 10);
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // When
        orderService.cancelOrder(orderId);

        // Then
        Order getOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 상태는 CANCEL");
        Assertions.assertEquals(10, item.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가한다.");
    }


    @Test
    public void exceedProducts() throws Exception{
        // Given
        Member member = createMember();
        Item item = createBook("JPA", 10000, 10);

        // 재고보다 많은 수량 입력
        int orderCount = 11;

        // When
        // Then
        Assertions.assertThrows(
                NotEnoughStockException.class,
                () -> orderService.order(member.getId(), item.getId(), orderCount));

    }



    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        entityManager.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("daiseek");
        member.setAddress(new Address("Incheon", "River", "123-123"));
        entityManager.persist(member);
        return member;
    }

}

