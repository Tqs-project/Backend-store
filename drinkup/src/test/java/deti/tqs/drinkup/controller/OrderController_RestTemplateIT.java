package deti.tqs.drinkup.controller;

import deti.tqs.drinkup.dto.OrderDto;
import deti.tqs.drinkup.model.Item;
import deti.tqs.drinkup.model.Order;
import deti.tqs.drinkup.model.User;
import deti.tqs.drinkup.repository.ItemRepository;
import deti.tqs.drinkup.repository.OrderRepository;
import deti.tqs.drinkup.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class OrderController_RestTemplateIT {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private OrderDto orderDto;
    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User("Manel123", "manel432@gmail.com", "boapass");
        userRepository.save(user);

        item = new Item("Smirnoff White Vodka", 500, 20, "Smirnoff", 55.6, "Very good");
        itemRepository.save(item);

        HashMap<String,Integer> items = new HashMap<>();
        items.put("Smirnoff White Vodka", 2);

        orderDto = new OrderDto(
                3L,
                null,
                "CARD",
                "WAITING",
                40.0,
                "Aveiro 3820-345",
                "Manel123",
                items
        );
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void whenCreateCustomerIsValid_thenCreateCustomer() {
        ResponseEntity<OrderDto> response = restTemplate.postForEntity(
                "/api/order", orderDto, OrderDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(
                HttpStatus.CREATED
        );

        List<Order> found = orderRepository.findAll();
        assertThat(found).extracting(Order::getCost).containsOnly(
                orderDto.getCost()
        );
    }
}