package deti.tqs.drinkup.service;

import deti.tqs.drinkup.dto.TokenDto;
import deti.tqs.drinkup.dto.UserDto;
import deti.tqs.drinkup.model.User;
import deti.tqs.drinkup.repository.UserRepository;
import deti.tqs.drinkup.utils.Utils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Log4j2
@Service
@Transactional
public class UserServiceImp implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    private final SecureRandom rand = new SecureRandom();

    @Override
    public UserDto registerUser(UserDto userDto) {
        var user = new User(
                userDto.getUsername(),
                userDto.getEmail(),
                encoder.encode(userDto.getPassword())
        );
        var ret = this.userRepository.saveAndFlush(user);

        return Utils.parseUserDto(ret);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        var user = this.userRepository.findByUsername(userDto.getUsername());

        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(encoder.encode(userDto.getPassword()));

        var ret = this.userRepository.saveAndFlush(user);

        return Utils.parseUserDto(ret);
    }

    @Override
    public TokenDto login(UserDto userDto) {
        User optUser;

        if (userDto.getUsername() != null) {
            optUser = this.userRepository.findByUsername(userDto.getUsername());
        } else if (userDto.getEmail() != null) {
            optUser = this.userRepository.findByEmail(userDto.getEmail());
        } else {
            return new TokenDto("", "Please provide username or email for authentication");
        }
        if (optUser==null) {
            log.debug("No user found");
            return new TokenDto("", "Bad authentication parameters");
        }

        var user = optUser;
        if (this.encoder.matches(userDto.getPassword(), user.getPassword())) {
            var token = this.encoder.encode(String.valueOf(rand.nextDouble()));

            user.setAuthToken(token);
            this.userRepository.saveAndFlush(user);

            return new TokenDto(token, "");
        }
        return new TokenDto("", "Bad authentication parameters");
    }
}
