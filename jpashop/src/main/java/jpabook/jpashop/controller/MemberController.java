package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping(value = "/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult bindingResult) {

        // Binding result : 해당 메서드를 수행할때 오류가 있으면, 다음 행동을 지정할 수 있음.
        if (bindingResult.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
        Member member = new Member();

        member.setName(memberForm.getName());
        member.setAddress(address);

        // memberService의 join() 메서드로 회원을 DB에 생성
        memberService.join(member);

        return "redirect:/";
    }


    @GetMapping(value = "members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}

//2025-05-01T21:45:54.000+09:00  INFO 7580 --- [nio-8080-exec-4] p6spy                                    : #1746103554000 | took 0ms | statement | connection 13| url jdbc:h2:tcp://localhost/~/jpashop
//insert into member (city,street,zipcode,name,member_id) values (?,?,?,?,?)
//insert into member (city,street,zipcode,name,member_id) values ('서울','서울거리','12345','정대식',2);

