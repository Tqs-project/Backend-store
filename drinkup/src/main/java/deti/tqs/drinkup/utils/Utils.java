package deti.tqs.drinkup.utils;

import deti.tqs.drinkup.dto.UserDto;
import deti.tqs.drinkup.model.User;

import java.util.ArrayList;

public class Utils {
    private Utils() {}

    public static UserDto parseUserDto(User user) {
        var orders = new ArrayList<Long>();

        user.getOrders().forEach(
                order -> orders.add(order.getId())
        );
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                "",
                orders
        );
    }
}
