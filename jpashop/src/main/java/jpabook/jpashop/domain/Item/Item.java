package jpabook.jpashop.domain.Item;

import jpabook.jpashop.Service.UpdateItemDto;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //한개의 테이블에 때려박겠다
@DiscriminatorColumn(name = "dtype") // 구분해주기 위함
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;

    //재고수량
    private int stockQuantity;

    public Item() {
    }



    @ManyToMany(mappedBy = "items")
    private List<Category> categories= new ArrayList<>();


    //==비지니스 로직==//
    //재고증가
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if ( restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity =  restStock;
    }

    public void changeItem(UpdateItemDto itemDto){
        this.name = itemDto.getName();
        this.price = itemDto.getPrice();
        this.stockQuantity = itemDto.getStockQuantity();
    }




}
