package deti.tqs.drinkup.controller;

import deti.tqs.drinkup.dto.TokenDto;
import deti.tqs.drinkup.dto.UserDto;
import deti.tqs.drinkup.dto.UserLoginDto;
import deti.tqs.drinkup.repository.UserRepository;
import deti.tqs.drinkup.service.UserService;
import deti.tqs.drinkup.utils.Utils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Log4j2
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        log.info("Saving user " + userDto.getUsername() + ".");

        return new ResponseEntity<>(userService.registerUser(userDto),
                HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<UserDto> updateUser(@RequestHeader String username,
                                                      @RequestHeader String idToken,
                                                      @RequestBody UserDto userDto) {

        var user = userRepository.findByUsername(username);
        if (user == null)
            return new ResponseEntity<>(new UserDto(), HttpStatus.UNAUTHORIZED);

        if (!idToken.equals(user.getAuthToken()))
            return new ResponseEntity<>(new UserDto(), HttpStatus.UNAUTHORIZED);

        log.info(String.format("Updating customer %s.", username));
        var user1 = new UserDto(
                null,
                userDto.getUsername(),
                userDto.getEmail(),
                userDto.getPassword(),
                new ArrayList<>()
        );
        return new ResponseEntity<>(userService.updateUser(user1),
                HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserLoginDto userDto) {
        log.info("Logging in user");

        var user = new UserDto();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());


        var response = this.userService.login(user);

        if (response.isEmpty())
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }


    @GetMapping()
    public ResponseEntity<UserDto> getUser(@RequestHeader String username,
                                        @RequestHeader String idToken) {
        var user = userRepository.findByUsername(username);

        if (user == null)
            return new ResponseEntity<>(new UserDto(), HttpStatus.UNAUTHORIZED);

        if (!idToken.equals(user.getAuthToken()))
            return new ResponseEntity<>(new UserDto(), HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(Utils.parseUserDto(user), HttpStatus.OK);
    }
}
