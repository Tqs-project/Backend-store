package deti.tqs.drinkup.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private Double price;

    @Column
    private String brand;

    @Column
    private Double alcoholContent ;

    @Column
    private String description;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<OrderItem> ordersWithItem;

    public Item() {}

    public Item(String name, Integer quantity, Double price, String brand, Double alcoholContent, String description) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.brand = brand;
        this.alcoholContent = alcoholContent;
        this.description = description;

        this.ordersWithItem = new ArrayList<>();
    }
}
