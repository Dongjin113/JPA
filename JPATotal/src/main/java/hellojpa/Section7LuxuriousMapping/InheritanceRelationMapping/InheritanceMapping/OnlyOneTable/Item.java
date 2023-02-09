package hellojpa.Section7LuxuriousMapping.InheritanceRelationMapping.InheritanceMapping.OnlyOneTable;

import javax.persistence.*;

//@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)//단일테이블은 SINGLE_TABLE로만 바꿔주면 적용이 완료된다
//@DiscriminatorColumn //DTYPE이 생성된다 default(엔티티명) (MOVIE)
//DiscriminatorColumn이 생략되어있어도 알아서 자동으로 생성이된다
public abstract class Item {
    /* 단일테이블의 장점
    상관이없는것들은 다 null로 나오고
     성능이 제일 잘나온다
     insert query문이 한개만 들어간다
     */

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
