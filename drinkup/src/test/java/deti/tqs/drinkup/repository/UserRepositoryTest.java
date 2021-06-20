package deti.tqs.drinkup.repository;

import deti.tqs.drinkup.model.User;
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
class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    private final User user1 = new User(
            "Maria",
            "maria@gmail.com",
            "password"
    );


    @BeforeEach
    void setUp() {
        this.testEntityManager.clear();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByEmail() {
        this.testEntityManager.persistAndFlush(user1);

        User res = this.userRepository.findByEmail(user1.getEmail());

        Assertions.assertThat(res).isEqualTo(
                user1
        );
    }

    @Test
    void findByUsername() {
        this.testEntityManager.persistAndFlush(user1);

        User res = this.userRepository.findByUsername(user1.getUsername());

        Assertions.assertThat(res).isEqualTo(
                user1
        );
    }


    @Test
    void testNotNullParams() {
        var noUsername = new User(
                null,
                "someEmail@gmail.com",
                "pass"
        );

        // verify null username exception
        Assertions.assertThatThrownBy(
                () -> this.testEntityManager.persistAndFlush(noUsername)
        ).isInstanceOf(PersistenceException.class);

        noUsername.setUsername("NowIHaveAName");
        noUsername.setEmail(null);

        // verify null email exception
        Assertions.assertThatThrownBy(
                () -> this.testEntityManager.persistAndFlush(noUsername)
        ).isInstanceOf(PersistenceException.class);
    }

    @Test
    void whenUniqueConstraintIsViolated_ThenReturnError() {
        var user2 = new User(
                user1.getUsername(),
                user1.getEmail(),
                user1.getPassword()
        );

        this.testEntityManager.persistAndFlush(user1);

        Assertions.assertThatThrownBy(
                () -> this.testEntityManager.persistAndFlush(user2)
        ).isInstanceOf(PersistenceException.class);
    }
}