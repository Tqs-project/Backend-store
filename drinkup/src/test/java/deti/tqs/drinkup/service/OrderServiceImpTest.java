package deti.tqs.drinkup.service;

import deti.tqs.drinkup.dto.OrderDto;
import deti.tqs.drinkup.model.Item;
import deti.tqs.drinkup.model.Order;
import deti.tqs.drinkup.model.OrderItem;
import deti.tqs.drinkup.model.User;
import deti.tqs.drinkup.repository.ItemRepository;
import deti.tqs.drinkup.repository.OrderRepository;
import deti.tqs.drinkup.repository.UserRepository;
import deti.tqs.drinkup.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class OrderServiceImpTest {

    @Mock(lenient = true)
    private OrderRepository orderRepository;

    @Mock(lenient = true)
    private UserRepository userRepository;

    @Mock(lenient = true)
    private ItemRepository itemRepository;

    @Mock(lenient = true)
    private Utils utils;

    @InjectMocks
    private OrderServiceImp orderServiceImp;

    User user;
    OrderDto orderDto;
    Order order;
    Item item;
    OrderItem orderItem;

    @BeforeEach
    void setUp() {
        user = new User("Manel123", "manel432@gmail.com", "boapass");

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

        item = new Item("Smirnoff White Vodka", 500, 20.0, "Smirnoff", 55.6, "Very good");

        orderItem = new OrderItem(item, 2);
        List<OrderItem> list = new ArrayList<>();
        list.add(orderItem);

        order = new Order(orderDto.getPaymentType(), user, orderDto.getCost(), orderDto.getLocation(), list);
        order.setId(orderDto.getId());

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createOrder_AddToDB_Test() throws IOException, InterruptedException, JSONException {
        Mockito.when(userRepository.findByUsername("Manel123")).thenReturn(java.util.Optional.ofNullable(user));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);
        Mockito.when(utils.requestWeDeliverAPI(Mockito.any(HttpRequest.class))).thenReturn(new JSONObject());

        var res = orderServiceImp.placeOrder(orderDto, "token");
        res.setOrderTimestamp(null);
        assertThat(res).isEqualTo(orderDto);
    }
}