package deti.tqs.drinkup.repository;

import deti.tqs.drinkup.model.Item;
import deti.tqs.drinkup.model.Order;
import deti.tqs.drinkup.model.OrderItem;
import deti.tqs.drinkup.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private Item item = new Item("Vinho do Porto",
            10,
            25.0,
            "Barros",
            0.2,
            "Porto Barros 2011 Vintage 75 Cl"
    );
    private OrderItem ordItem;
    private List<OrderItem> lista = new ArrayList<>();
    private Order order;
    private User user1 = new User(
            "Maria",
            "maria@gmail.com",
            "password"
    );

    @BeforeEach
    void setUp() {
        this.entityManager.clear();
        this.entityManager.persistAndFlush(item);
        this.entityManager.persistAndFlush(user1);
        System.out.println("user " + user1);
        System.out.println("item " + item);

        order= new Order(
                "MB",
                user1,
                25.0,
                "Rua do Carregueiro, Oliveirinha",
                lista);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findOrdersByUser() {
        this.entityManager.persistAndFlush(order);

        List<Order> res = this.orderRepository.findOrdersByUser(order.getUser());

        Assertions.assertThat(res.get(0)).isEqualTo(order);

    }
}