package jpabook.jpashop;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hello {
    private String hello;
}

class HelloTest {
    Hello hello = new Hello();

    public Hello getHello() {
        return hello;
    }

}