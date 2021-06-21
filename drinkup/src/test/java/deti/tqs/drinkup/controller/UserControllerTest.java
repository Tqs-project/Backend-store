package deti.tqs.drinkup.controller;

import deti.tqs.drinkup.dto.TokenDto;
import deti.tqs.drinkup.dto.UserDto;
import deti.tqs.drinkup.dto.UserLoginDto;
import deti.tqs.drinkup.model.User;
import deti.tqs.drinkup.repository.UserRepository;
import deti.tqs.drinkup.service.UserService;
import deti.tqs.drinkup.utils.JsonUtil;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest_WithMockServiceIT {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    private User user;
    private UserDto userDto;
    private UserDto userDtoResponse;

    @BeforeEach
    void setUp() {
        user = new User(
                "Albert",
                "albert@gmail.com",
                "password"
        );
        user.setId(3L);

        user.setAuthToken("token");

        userDto = new UserDto(
                        null,
                        "Albert",
                        "albert@gmail.com",
                        "password",
                        new ArrayList<>()

                );

        userDtoResponse = new UserDto(
                    3L,
                        "Albert",
                        "albert@gmail.com",
                        "",
                        new ArrayList<>()
                );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void whenPostUser_thenCreateUser() throws Exception{
        Mockito.when(userService.registerUser(userDto))
                .thenReturn(userDtoResponse);

        mvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(userDto))
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", CoreMatchers.is(user.getUsername())))
                .andExpect(jsonPath("$.password", CoreMatchers.is("")));

        Mockito.verify(userService, Mockito.times(1)).registerUser(userDto);
    }

    @Test
    void whenPutUser_thenReturnUserUpdated() throws Exception {

        userDto.setEmail("albert22@gmail.com");
        userDtoResponse.setEmail("albert22@gmail.com");
        Mockito.when(userService.updateUser(userDto))
                .thenReturn(userDtoResponse);

        Mockito.when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.ofNullable(user));

        mvc.perform(
                put("/api/users")
                        .header("username", userDto.getUsername())
                        .header("idToken", "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(userDto))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.username", CoreMatchers.is(userDto.getUsername())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(userDto.getEmail())));

        Mockito.verify(userService, Mockito.times(1)).updateUser(userDto);
    }

    @Test
    void whenPostUserLogin_ThenReturnToken() throws Exception {
        var token = new TokenDto(user.getId(), "encrypted-token", "");

        var login = new UserLoginDto(
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );

        var userLogin = new UserDto();
        userLogin.setUsername(login.getUsername());
        userLogin.setEmail(login.getEmail());
        userLogin.setPassword(login.getPassword());

        Mockito.when(userService.login(userLogin))
                .thenReturn(token);

        mvc.perform(
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(login))
        ).andExpect(status().isAccepted())
                .andExpect(jsonPath("$.token", CoreMatchers.is(token.getToken())));

        Mockito.verify(userService, Mockito.times(1)).login(userLogin);
    }

    @Test
    void whenPostUserLoginWithError_thenReturnEmptyToken() throws Exception {
        var token = new TokenDto(null, "", "Some error occurred");

        var login = new UserLoginDto(
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );

        var userLogin = new UserDto();
        userLogin.setUsername(login.getUsername());
        userLogin.setEmail(login.getEmail());
        userLogin.setPassword(login.getPassword());

        Mockito.when(userService.login(userLogin))
                .thenReturn(token);

        mvc.perform(
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(login))
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.token", CoreMatchers.is("")))
                .andExpect(jsonPath("$.errorMessage", CoreMatchers.is(token.getErrorMessage())));

        Mockito.verify(userService, Mockito.times(1)).login(userLogin);
    }
}