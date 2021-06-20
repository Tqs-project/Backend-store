package deti.tqs.drinkup.repository;

import deti.tqs.drinkup.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByName(String name);
    Optional<Item> findById(Long id);
    boolean existsByName(String name);

}
