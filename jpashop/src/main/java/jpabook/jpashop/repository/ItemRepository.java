package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager entityManager;

    public void save(Item item) {
        // persist를 이용해 새로 등록하는 로직(업데이트와 유사)
        if (item.getId() == null) {
            entityManager.persist(item);
        } else {
            // 강제 업데이트
            entityManager.merge(item);
        }
    }

    public Item findOne(Long id) {
        return entityManager.find(Item.class, id);
    }

    public List<Item> findAll() {
        return entityManager
                .createQuery("SELECT i FROM Item i", Item.class)
                .getResultList();
    }
}
