package deti.tqs.drinkup.model;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private Integer quantity;

    public OrderItem(){}

    public OrderItem(Item item, Integer quantity){
        this.item = item;
        this.quantity = quantity;
    }
}
