package jpabook.jpashop.Service;

import jpabook.jpashop.domain.Item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;


@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void updateTest() throws Exception{
        Book book = em.find(Book.class, 1L );
        //TX
        book.setName("asdf");

        //변경감지

    }
}
