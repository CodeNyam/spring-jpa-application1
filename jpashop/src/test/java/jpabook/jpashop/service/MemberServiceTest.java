package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

//@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;



    @Test
//    @Rollback(value = false) // DB 확인용
    public void join() throws Exception{
        // given
        Member member = new Member();
        member.setName("daiseek");

        // when
        Long savedId = memberService.join(member);

        // then
        Assertions.assertEquals(member, memberRepository.findOne(savedId));
        // 같은 영속성 컨텍스트 내에서 같은 아이디가 작동함
    }

    @Test
    public void 중복회원예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("daiseek1");

        Member member2 = new Member();
        member2.setName("daiseek1");

        // when 예외 발생
        memberService.join(member1);
        try {
            memberService.join(member2);
        } catch (IllegalStateException e) {
            return;
        }

        // then
        fail("예외가 발생해야 한다.");

    }

    @Test
    public void findOne() throws Exception {
        // given

        // when

        // then
    }
}