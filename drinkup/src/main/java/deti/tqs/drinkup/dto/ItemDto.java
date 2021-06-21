package deti.tqs.drinkup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;

    private String name;

    private Integer quantity;

    private Double price;

    private String brand;

    private Double alcoholContent ;

    private String description;
}
