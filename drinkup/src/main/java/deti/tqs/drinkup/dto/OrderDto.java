package deti.tqs.drinkup.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;

    private Timestamp orderTimestamp;

    private String paymentType;

    private String status;

    private Double cost;

    private String location;

    private String userName;

    private HashMap<String,Integer> items;

}

