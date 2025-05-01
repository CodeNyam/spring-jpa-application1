package jpabook.jpashop.repository;

import ch.qos.logback.core.util.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager entityManager;
    // Q. @PersistenceContext를 명시하지 않은 이유?
    // 스프링 부트를 이용하기 때문에, 알아서 엔티티매니저와 영속성을 관리해줌!
    // 하지만, 협업시 애노테이션으로 명시해서 확실하게 해주는게 좋음.

    public void save(Order order) {
        entityManager.persist(order);
    }

    public Order findOne(Long id) {
        return entityManager.find(Order.class, id);
    }

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> o = criteriaQuery.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER);

        List<Predicate> crteria = new ArrayList<>();

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = criteriaBuilder.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            crteria.add(status);
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = criteriaBuilder.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            crteria.add(name);
        }

        criteriaQuery.where(criteriaBuilder.and(crteria.toArray(new Predicate[crteria.size()])));
        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery).setMaxResults(1000);
        return query.getResultList();

//        entityManager.createQuery("SELECT o FROM order o join o.member m" +
//                "Where o.status = :status" +
//                "And m.name LIKE :name"
//                , Order.class)
//                .setParameter("status", orderSearch.getOrderStatus())
//                .setParameter("name", orderSearch.getMemberName())
//                .setFirstResult(100)
//                .setMaxResults(1000)    // 최대 1000건
//                .getResultList();
    }
}
