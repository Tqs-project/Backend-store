package deti.tqs.drinkup.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer price;

    @Column
    private String brand;

    @Column
    private Double alcoholContent ;

    @Column
    private String description;

    public Item() {}

    public Item(String name, Integer quantity, Integer price, String brand, Double alcoholContent, String description) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.brand = brand;
        this.alcoholContent = alcoholContent;
        this.description = description;

    }
}
