package deti.tqs.drinkup.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import deti.tqs.drinkup.dto.OrderDto;

import java.io.IOException;
import java.util.List;

public interface OrderService {
    public OrderDto placeOrder(OrderDto orderDto, String token) throws IOException, InterruptedException;
        //Simples, basicamente criar uma order localmente e chamar a WeDeliver pra criar lá também
    //public String checkOrderState(int id);
        //O state retornado pode ser um simples string ("waiting for rider", "on it's wasy" or "delivered").
        //Por outro lado tambem se podia fazer um model "Status" que teria detalhes tipo assigned rider e tudo mais, mas n sei se seria congruente com a WeDeliver.
    //public List<OrderDto> getAllOrders(boolean active);
        //Ir buscar todas as orders, se o active estiver a true retornar apenas as orders não delivered

}
