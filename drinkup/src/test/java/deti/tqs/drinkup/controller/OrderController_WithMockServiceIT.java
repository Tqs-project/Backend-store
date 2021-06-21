package deti.tqs.drinkup.controller;

import deti.tqs.drinkup.dto.OrderDto;
import deti.tqs.drinkup.model.Item;
import deti.tqs.drinkup.model.User;
import deti.tqs.drinkup.repository.ItemRepository;
import deti.tqs.drinkup.repository.UserRepository;
import deti.tqs.drinkup.service.OrderService;
import deti.tqs.drinkup.util.Utils;
import deti.tqs.drinkup.utils.JsonUtil;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@WebMvcTest(OrderController.class)
class OrderController_WithMockServiceIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ItemRepository itemRepository;

    private User user;
    private Item item;

    private Utils utils;

    private OrderDto orderDto;
    private OrderDto orderResponse;

    @BeforeEach
    void setUp() {
        utils = new Utils();

        user = new User("Manel123", "manel432@gmail.com", "boapass");

        item = new Item("Smirnoff White Vodka", 500, 20, "Smirnoff", 55.6, "Very good");

        HashMap<String,Integer> items = new HashMap<>();
        items.put("Smirnoff White Vodka", 2);

        orderDto = new OrderDto(
                null,
                null,
                "CARD",
                null,
                40.0,
                "Aveiro 3820-345",
                "Manel123",
                items
        );

        orderResponse = new OrderDto(
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
    }

    @Test
    void whenPostOrder_thenCreateOrder() throws Exception{
        Mockito.when(userRepository.findByUsername("Manel123")).thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findByName("Smirnoff White Vodka")).thenReturn(item);
        Mockito.when(itemRepository.existsByName("Smirnoff White Vodka")).thenReturn(true);
        Mockito.when(orderService.placeOrder(any(OrderDto.class), any(String.class)))
                .thenReturn(orderResponse);

        mvc.perform(
                post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(orderDto))
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.is(3)))
                .andExpect(jsonPath("$.status", CoreMatchers.is("WAITING")))
                .andExpect(jsonPath("$.cost", CoreMatchers.is(40.0)));

    }

    @Test
    void whenUserInvalid_thenUnauthorized() throws Exception{
        Mockito.when(userRepository.findByUsername("Manel123")).thenReturn(Optional.empty());
        Mockito.when(itemRepository.findByName("Smirnoff White Vodka")).thenReturn(item);
        Mockito.when(itemRepository.existsByName("Smirnoff White Vodka")).thenReturn(true);
        Mockito.when(orderService.placeOrder(any(OrderDto.class), any(String.class)))
                .thenReturn(orderResponse);

        mvc.perform(
                post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(orderDto))
        ).andExpect(status().isUnauthorized());

    }

    @Test
    void whenItemInvalid_thenNotAcceptable() throws Exception{
        Mockito.when(userRepository.findByUsername("Manel123")).thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findByName("Smirnoff White Vodka")).thenReturn(item);
        Mockito.when(itemRepository.existsByName("Smirnoff White Vodka")).thenReturn(false);
        Mockito.when(orderService.placeOrder(any(OrderDto.class), any(String.class)))
                .thenReturn(orderResponse);

        mvc.perform(
                post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(orderDto))
        ).andExpect(status().isNotAcceptable());

    }

    @Test
    void whenPriceInvalid_thenNotAcceptable() throws Exception{
        Mockito.when(userRepository.findByUsername("Manel123")).thenReturn(Optional.ofNullable(user));
        item.setPrice(25);
        Mockito.when(itemRepository.findByName("Smirnoff White Vodka")).thenReturn(item);
        Mockito.when(itemRepository.existsByName("Smirnoff White Vodka")).thenReturn(true);
        Mockito.when(orderService.placeOrder(any(OrderDto.class), any(String.class)))
                .thenReturn(orderResponse);

        mvc.perform(
                post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(orderDto))
        ).andExpect(status().isNotAcceptable());

    }
}