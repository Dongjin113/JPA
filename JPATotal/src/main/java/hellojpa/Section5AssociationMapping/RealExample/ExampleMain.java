package hellojpa.Section5AssociationMapping.RealExample;

import javax.persistence.*;

public class ExampleMain {

    public static void main (String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{

            Order order = new Order();
            order.addOrderItem(new OrderItem());

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();

    }
}
