package deti.tqs.drinkup.utils;

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

        var parsing = Utils.parseUserDto(user);
        Assertions.assertThat(
                parsing
        ).extracting("email").isEqualTo(user.getEmail());

        Assertions.assertThat(
                parsing
        ).extracting("username").isEqualTo(user.getUsername());
    }
}