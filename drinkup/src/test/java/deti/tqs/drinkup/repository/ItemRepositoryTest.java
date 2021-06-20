package deti.tqs.drinkup.repository;

import deti.tqs.drinkup.model.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ItemRepository itemRepository;

    private final Item item = new Item(
            "Vinho do Porto",
            10,
            25.0,
            "Barros",
            0.2,
            "Porto Barros 2011 Vintage 75 Cl"
    );

    @BeforeEach
    void setUp() {
        this.testEntityManager.clear();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByName() {
        this.testEntityManager.persistAndFlush(item);

        Item res = this.itemRepository.findByName(item.getName());

        Assertions.assertThat(res).isEqualTo(
                item
        );
    }

    @Test
    void findById() {
        this.testEntityManager.persistAndFlush(item);

        Item res = this.itemRepository.findById(item.getId()).get();

        Assertions.assertThat(res).isEqualTo(
                item
        );
    }
}