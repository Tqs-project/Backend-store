package deti.tqs.drinkup.controller;

import deti.tqs.drinkup.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //public methods for endpoints, same as service


    //private TokenDto login();
    //method that will be called in each endpoint to ensure the DrinkUp is authenticated, probably will be moved to an "utils" folder

}
