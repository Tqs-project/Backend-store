package deti.tqs.drinkup.controller;

import deti.tqs.drinkup.dto.OrderDto;
import deti.tqs.drinkup.model.User;
import deti.tqs.drinkup.repository.UserRepository;
import deti.tqs.drinkup.service.OrderService;
import deti.tqs.drinkup.util.Utils;
import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    private final String token = Utils.getAuthToken();

    public OrderController() throws JSONException, IOException, InterruptedException {
    }

    @PostMapping()
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) throws IOException, InterruptedException {

        var user = userRepository.findByUsername(orderDto.getUserName());
        if (user.isEmpty())
            return new ResponseEntity<>(new OrderDto(), HttpStatus.UNAUTHORIZED);

        log.info("Saving order " + orderDto.getLocation() + ".");

        return new ResponseEntity<>(this.orderService.placeOrder(orderDto, token),
                HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrderState(@PathVariable int id) throws IOException, InterruptedException, JSONException {

        return new ResponseEntity<>(this.orderService.checkOrderState(id, token),
                HttpStatus.ACCEPTED);

    }

    @GetMapping()
    public ResponseEntity<List<OrderDto>> getOrderState(@RequestBody boolean active) throws IOException, InterruptedException, JSONException {

        return new ResponseEntity<>(this.orderService.getAllOrders(active, token),
                HttpStatus.ACCEPTED);

    }
}
