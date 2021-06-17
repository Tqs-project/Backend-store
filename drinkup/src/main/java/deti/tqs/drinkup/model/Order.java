package deti.tqs.drinkup.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Timestamp orderTimestamp;

    @Column(name = "payment_type",columnDefinition = "VARCHAR(20) CHECK (payment_type IN ('MB', 'PAYPAL', 'MBWAY'))")
    private String paymentType;

    @Column(columnDefinition = "Decimal(10, 2)")
    private Double cost;

    @Column(columnDefinition = "VARCHAR(20) CHECK (status IN ('WAITING', 'DELIVERING', 'DELIVERED', 'NOT DELIVERED))")
    private String status;

    @Column(columnDefinition = "VARCHAR(100)")
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany
    private List<OrderItem> orders;

    public Order() {}

    public Order(String paymentType, User user, String location) {
        this.paymentType = paymentType;
        this.user = user;
        this.location = location;

        this.orderTimestamp = new Timestamp(System.currentTimeMillis());
        this.status = "WAITING";
        orders = new ArrayList<>();
    }

    public Order(String paymentType, User user, String location, List<OrderItem> orders) {
        this.paymentType = paymentType;
        this.user = user;
        this.location = location;
        this.orders = orders;

        this.orderTimestamp = new Timestamp(System.currentTimeMillis());
        this.status = "WAITING";
        orders = new ArrayList<>();
    }
}
