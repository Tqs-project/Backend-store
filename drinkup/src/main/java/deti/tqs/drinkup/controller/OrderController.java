package deti.tqs.drinkup.controller;

import deti.tqs.drinkup.dto.OrderDto;
import deti.tqs.drinkup.service.OrderService;
import deti.tqs.drinkup.util.Utils;
import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Log4j2
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private final String token = Utils.getAuthToken();

    public OrderController() throws JSONException, IOException, InterruptedException {
    }

    //public methods for endpoints, same as service

    @PostMapping()
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) throws IOException, InterruptedException {

        //TODO check if user exists

        log.info("Saving order " + orderDto.getLocation() + ".");

        return new ResponseEntity<>(this.orderService.placeOrder(orderDto, token),
                HttpStatus.CREATED);

    }
}
