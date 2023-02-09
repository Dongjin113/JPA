package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    public void save(){
//      springJPA는 pk 값을 통해 새로운 값인지 아닌지 확인하고 새로운 값이면 em.persist가
//      pk값이 이미 존재하면 em.merge가  실행이 되는데 이때 db에 확인을 하기위해 select가 일어난후 값이 없는지 확인 후 insert가 일어난다
//      이때문에 비효율을 방지하기 위해 entitydp Persistable을 사용한다
        Item item= new Item("3");
        System.out.println("isnew = "+item.isNew());
        itemRepository.save(item);
    }
}
