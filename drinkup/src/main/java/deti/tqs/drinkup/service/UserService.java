package deti.tqs.drinkup.service;

import deti.tqs.drinkup.dto.TokenDto;
import deti.tqs.drinkup.dto.UserDto;

public interface UserService {
    UserDto registerUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);
    TokenDto login(UserDto userDto);
}
