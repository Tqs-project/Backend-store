package deti.tqs.drinkup.controller;

import deti.tqs.drinkup.dto.OrderDto;
import deti.tqs.drinkup.model.Item;
import deti.tqs.drinkup.model.User;
import deti.tqs.drinkup.repository.ItemRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private final Utils utils = new Utils();

    private final String token = utils.getAuthToken();

    public OrderController() throws JSONException, IOException, InterruptedException {
    }

    @PostMapping()
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) throws IOException, InterruptedException {

        var user = userRepository.findByUsername(orderDto.getUserName());
        if (user.isEmpty())
            return new ResponseEntity<>(new OrderDto(), HttpStatus.UNAUTHORIZED);

        if(!checkItemIntegrity(orderDto)){
            return new ResponseEntity<>(new OrderDto(), HttpStatus.NOT_ACCEPTABLE);
        }

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
    public ResponseEntity<List<OrderDto>> getAllOrder(@RequestBody boolean active) throws IOException, InterruptedException, JSONException {

        return new ResponseEntity<>(this.orderService.getAllOrders(active, token),
                HttpStatus.ACCEPTED);

    }

    private boolean checkItemIntegrity(OrderDto orderDto){
        HashMap<String, Integer> items = orderDto.getItems();
        double trueCost = 0.0;

        for (Map.Entry<String,Integer> entry : items.entrySet()) {
            if(!itemRepository.existsByName(entry.getKey())){
                return false;
            }
            trueCost+= itemRepository.findByName(entry.getKey()).getPrice() * entry.getValue();
        }
        return orderDto.getCost()==trueCost;
    }
}
