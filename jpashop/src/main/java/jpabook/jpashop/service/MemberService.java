package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
// 스프링의 트랜잭션 사용
@Transactional(readOnly = true) // JPA를 사용하면 모든 로직은 트랜잭션 안에서 이루어져야함
// readOnly : true -> 영속성 컨텍스트로 인한 이점, 읽기 전용이므로 리소스를 최적화하여 조회 가능
// 당연히 읽기 전용 엔티티, 필드가 아닐 경우는 readOnly를 지정하면 안된다.
// 여기선 모든 메서드에서 readOnly가 적용된다.
public class MemberService {

//    @Autowired  // 필드주입법
//    MemberRepository memberRepository;
    // 단점 1. 테스트 시 주입된 객체를 바꿀 수 없다...
    // 따라서 롬복을 이용해 생성자 주입법을 이용하자.

    private final MemberRepository memberRepository;
    

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 회원이 중복된 회원인지 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // Note. 비즈니스 로직상 유효성 검사 메서드가 있어도 추가 대처가 필요하다
    // 원인은 동시성 이슈(, 멀티 스레드) 때문!
    // 예를 들어 두 명이 동시에 멤버1을 가입시키는 경우, 똑같은 이름의 멤버가 저장된다.
    // 따라서 Unique key 등 추가적인 제약을 걸어둘 필요가 있다.


    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }


}
