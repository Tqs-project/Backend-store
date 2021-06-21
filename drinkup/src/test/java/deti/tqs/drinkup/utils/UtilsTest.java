package deti.tqs.drinkup.utils;

import deti.tqs.drinkup.model.Order;
import deti.tqs.drinkup.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void parseUserDto() {
        var user = new User(
                "Jorge",
                "jorge@gmail.com",
                "password"
        );

        var order1 = new Order();
        order1.setId(10L);
        var order2 = new Order();
        order2.setId(11L);

        user.setOrders(Arrays.asList(order1, order2));

        var parsing = Utils.parseUserDto(user);
        Assertions.assertThat(
                parsing
        ).extracting("email").isEqualTo(user.getEmail());

        Assertions.assertThat(
                parsing
        ).extracting("username").isEqualTo(user.getUsername());

        Assertions.assertThat(
                parsing
        ).extracting("orders").isEqualTo(Arrays.asList(10L, 11L));
    }
}