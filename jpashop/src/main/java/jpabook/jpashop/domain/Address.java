package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    //값타입이라는것은 변경이 불가능하게 설계해야한다 생성자에서 값을 모두 초기화해 변경 불가능한 클래스를 만들자.
    //그래서setter는 생성을 하지 않는다
    //JPA 스펙상 엔티티나 임베디드타입은 자바 기본생성자를
    //public 또는 protected로 설정해야 한다 public으로 두는 것 보다는 protected로 설정하는 것이 그나마 더 안전하다
    private String city;
    private String street;
    private String zipcode;

    //protected는 사용 가능하게 해준다
    protected  Address(){}

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
