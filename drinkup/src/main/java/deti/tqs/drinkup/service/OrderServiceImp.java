package deti.tqs.drinkup.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import deti.tqs.drinkup.dto.OrderDto;
import deti.tqs.drinkup.model.Order;
import deti.tqs.drinkup.model.OrderItem;
import deti.tqs.drinkup.repository.ItemRepository;
import deti.tqs.drinkup.repository.OrderRepository;
import deti.tqs.drinkup.repository.UserRepository;
import deti.tqs.drinkup.util.Utils;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@Transactional
public class OrderServiceImp implements OrderService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    private final Utils utils = new Utils();
    private static final String BASEURI = "http://webmarket-314811.oa.r.appspot.com/";

    public OrderDto placeOrder(OrderDto orderDto, String token) throws IOException, InterruptedException {

        var body = new HashMap<String, Object>();
        body.put("username", "DrinkUp");
        body.put("paymentType", orderDto.getPaymentType());
        body.put("cost", orderDto.getCost());
        body.put("location", orderDto.getLocation());

        var objectMapper = new ObjectMapper();
        var requestBody = objectMapper.writeValueAsString(body);

        HttpRequest request = null;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(BASEURI+"api/order"))
                    .setHeader("username", "DrinkUp")
                    .setHeader("idToken", token)
                    .setHeader("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        JSONObject jsonResponse = utils.requestWeDeliverAPI(request);

        if(jsonResponse!=null){
            List<OrderItem> orders = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : orderDto.getItems().entrySet()) {
                new OrderItem(itemRepository.findByName(entry.getKey()), entry.getValue());
            }
            var user = userRepository.findByUsername(orderDto.getUserName());
            if(user.isPresent()){
                var order = new Order(orderDto.getPaymentType(), user.get(), orderDto.getCost(), orderDto.getLocation(), orders);
                var ret = orderRepository.save(order);
                return new OrderDto(
                        ret.getId(),
                        ret.getOrderTimestamp(),
                        ret.getPaymentType(),
                        ret.getStatus(),
                        ret.getCost(),
                        ret.getLocation(),
                        ret.getUser().getUsername(),
                        orderDto.getItems()
                );
            }
        }
        return null;
    }

    public String checkOrderState(int id, String token) throws IOException, InterruptedException, JSONException {

        HttpRequest request = null;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(BASEURI+"api/customer/orders/"+id))
                    .setHeader("username", "DrinkUp")
                    .setHeader("idToken", token)
                    .setHeader("Content-Type", "application/json")
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        JSONObject jsonResponse = utils.requestWeDeliverAPI(request);

        if(jsonResponse!=null){
            return (String) jsonResponse.get("status");
        }
        return null;
    }

    public List<OrderDto> getAllOrders(boolean active, String token) throws IOException, InterruptedException, JSONException {
        HttpRequest request = null;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(BASEURI+"api/customer/orders/"))
                    .setHeader("username", "DrinkUp")
                    .setHeader("idToken", token)
                    .setHeader("Content-Type", "application/json")
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        JSONArray jsonResponse = utils.arrayRequestWeDeliverAPI(request);

        List<OrderDto> list = new ArrayList<>();

        if(jsonResponse!=null){
            for (var i = 0; i < jsonResponse.length(); i++) {
                 var o = jsonResponse.getJSONObject(i);
                 var orderDto = new OrderDto();
                 orderDto.setCost((Double) o.get("cost"));
                 orderDto.setLocation((String) o.get("location"));
                 orderDto.setUserName((String) o.get("username"));
                 orderDto.setStatus((String) o.get("status"));
                 orderDto.setId(Long.valueOf((Integer) o.get("id")));
                 if (active){
                     if ((o.get("status") != "DELIVERED")){
                         list.add(orderDto);
                     }
                 }else{
                     list.add(orderDto);
                 }
            }
        }
        return list;
    }
}
