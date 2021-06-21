package deti.tqs.drinkup.service;

import deti.tqs.drinkup.dto.TokenDto;
import deti.tqs.drinkup.dto.UserDto;
import deti.tqs.drinkup.model.User;
import deti.tqs.drinkup.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

    @Mock(lenient = true)
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserServiceImp userService;

    private UserDto userCreateDto;
    private UserDto userCreateDtoRet;

    private User user;
    private User userFromDb;

    private TokenDto token;

    @BeforeEach
    void setUp() {
        userCreateDtoRet = new UserDto(
                3L,
                "Maria",
                "maria@gmail.com",
                "",
                new ArrayList<>()
        );

        userCreateDto = new UserDto();
        userCreateDto.setUsername("Maria");
        userCreateDto.setEmail("maria@gmail.com");
        userCreateDto.setPassword("");

        user = new User(
                userCreateDto.getUsername(),
                userCreateDto.getEmail(),
                userCreateDto.getPassword() + "-encoded"
        );

        userFromDb = new User(
                userCreateDto.getUsername(),
                userCreateDto.getEmail(),
                userCreateDto.getPassword() + "-encoded"
        );
        userFromDb.setId(3L);

        token = new TokenDto(3L,"encrypted-token", "");

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void registerUserTest() {
        Mockito.when(encoder.encode(userCreateDto.getPassword())).thenReturn(
                userCreateDto.getPassword() + "-encoded");

        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(
                userFromDb
        );

        assertThat(userService.registerUser(userCreateDto)).isEqualTo(
                userCreateDtoRet
        );
    }

    @Test
    void updateUser() {
        userCreateDto.setEmail("maria1@gmail.com");
        userCreateDtoRet.setEmail(userCreateDto.getEmail());
        user.setEmail(userCreateDto.getEmail());
        userFromDb.setEmail(userCreateDto.getEmail());

        Mockito.when(encoder.encode(userCreateDto.getPassword())).thenReturn(
                userCreateDto.getPassword() + "-encoded");


        Mockito.when(userRepository.findByUsername(userCreateDto.getUsername()))
                .thenReturn(Optional.ofNullable(user));

        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(
                userFromDb
        );

        assertThat(userService.updateUser(userCreateDto)).isEqualTo(
                userCreateDtoRet
        );
    }

    @Test
    void loginWithUsername() {
        Mockito.when(encoder.encode(ArgumentMatchers.anyString())).thenReturn(
                "encrypted-token"
        );
        token.setId(userCreateDto.getId());

        Mockito.when(encoder.matches(userCreateDto.getPassword(),
                userCreateDto.getPassword() + "-encoded"))
                .thenReturn(true);

        Mockito.when(userRepository.findByUsername(userCreateDto.getUsername()))
                .thenReturn(Optional.ofNullable(user));

        assertThat(userService.login(userCreateDto))
                .isEqualTo(token);

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(
                userCreateDto.getUsername()
        );
        Mockito.verify(userRepository, Mockito.times(0)).findByEmail(
                userCreateDto.getEmail()
        );
    }

    @Test
    void loginWithEmail() {
        Mockito.when(encoder.encode(ArgumentMatchers.anyString())).thenReturn(
                "encrypted-token"
        );
        token.setId(userCreateDto.getId());

        Mockito.when(encoder.matches(userCreateDto.getPassword(),
                userCreateDto.getPassword() + "-encoded"))
                .thenReturn(true);


        userCreateDto.setUsername(null);

        Mockito.when(userRepository.findByEmail(userCreateDto.getEmail()))
                .thenReturn(user);

        assertThat(userService.login(userCreateDto))
                .isEqualTo(token);

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(
                userCreateDto.getEmail()
        );
        Mockito.verify(userRepository, Mockito.times(0)).findByUsername(
                userCreateDto.getUsername()
        );
    }
}