package deti.tqs.drinkup.repository;

import deti.tqs.drinkup.model.Order;
import deti.tqs.drinkup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByOrderTimestampAfter(Timestamp timestamp);
    List<Order> findOrdersByOrderTimestampBefore(Timestamp timestamp);
    List<Order> findOrdersByUser(User user);
}
