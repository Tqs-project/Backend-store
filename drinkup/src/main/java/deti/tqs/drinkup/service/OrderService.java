package deti.tqs.drinkup.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import deti.tqs.drinkup.dto.OrderDto;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public interface OrderService {
    public OrderDto placeOrder(OrderDto orderDto, String token) throws IOException, InterruptedException;
    public String checkOrderState(int id, String token) throws IOException, InterruptedException, JSONException;
    public List<OrderDto> getAllOrders(boolean active, String token) throws IOException, InterruptedException, JSONException;

}
