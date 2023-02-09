package hellojpa.Section7LuxuriousMapping.InheritanceRelationMapping.InheritanceMapping;

import javax.persistence.*;

//@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn //DTYPE이 생성된다 default(엔티티명) (MOVIE)
//Item table이 생성되지 않는다
//Item의 변수들이 상속받은 테이블 안에 생성이 된다
//좋은 방법은 아니다 거의 사용X
public abstract class Item {

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
