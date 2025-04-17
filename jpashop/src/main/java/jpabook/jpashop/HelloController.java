package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(Model model) {
        // model에서 데이터를 받아서 사용
        // hello.html에서 data 자리에 hello! 매핑
        model.addAttribute("data", "hello!");
        // hello 파일 반환
        return "hello";
    }

}