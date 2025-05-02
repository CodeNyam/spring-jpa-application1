package jpabook.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;

}

// Q. 엔티티의 필드 구성이 Member와 비슷한데 왜 Form을 또 만들었을까?
// A. 엔티티의 필드 구성이 약간 다르기 때문이다.
// 회원가입에 필요한 도시, 거리, 이름, 우편번호 외에
// member 클래스에는 아이디 필드와, 오더스 객체가 있다.
// 또, 유효성 검증을 위해 @Valid 애노테이션을 추가하면 코드가 지저분해진다.
// 따라서, 엔티티를 바로 쓸 때 컨트롤러를 통해 들어오는 데이터 구성이 다르면, 추가로 조정해야할 수도 있다.
// 폼 클래스를 만들고, 이를 이용해 데이터를 받는게 편하다!