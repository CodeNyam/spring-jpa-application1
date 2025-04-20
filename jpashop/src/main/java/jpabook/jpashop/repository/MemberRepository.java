package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    // 엔티티 매니저 주입
    private EntityManager entityManager;

//    public MemberRepository(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }
    // 엔티티 주입 생성자는 Spring Boot가 제공해줘서 작성할 필요 없음!



    public void save(Member member) {
        // JPA의 엔티티 매니저를 통해 멤버 저장
        entityManager.persist(member); // -> DB에서 데이터가 저장되고, 관리됨
    }

    public Member findOne(Long id) {
        // JPA의 단건 조회
        // (Type, PK)로 매개값을 준다.
        return entityManager.find(Member.class, id);
    }

    public List<Member> findAll() {
        // createQuery, JPQL를 이용함
        // JPQL : 엔티티 객체를 대상으로 쿼리를 실행
        // FROM문이 테이블이 아닌 객체를 대상으로 함
        return entityManager.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return entityManager.createQuery("SELECT m FROM Member m WHERE m.name = :name", Member.class) // :name으로 파라미터 바인딩
                .setParameter("name", name)
                .getResultList();
    }

}
