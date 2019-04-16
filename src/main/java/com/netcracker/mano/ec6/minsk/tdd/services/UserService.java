package com.netcracker.mano.ec6.minsk.tdd.services;

import com.netcracker.mano.ec6.minsk.tdd.converters.PasswordCodec;
import com.netcracker.mano.ec6.minsk.tdd.converters.UserConverter;
import com.netcracker.mano.ec6.minsk.tdd.dao.UserDao;
import com.netcracker.mano.ec6.minsk.tdd.dto.RoleDto;
import com.netcracker.mano.ec6.minsk.tdd.dto.UserDto;
import com.netcracker.mano.ec6.minsk.tdd.entities.User;
import com.netcracker.mano.ec6.minsk.tdd.exceptions.EntityNotFoundException;
import com.netcracker.mano.ec6.minsk.tdd.exceptions.ExistingUsernameRegistrationException;
import com.netcracker.mano.ec6.minsk.tdd.exceptions.PasswordMismatchException;
import com.netcracker.mano.ec6.minsk.tdd.exceptions.SimplePasswordRegistrationException;
import java.util.Collection;
import java.util.stream.Collectors;

public class UserService {

  private UserDao userDao = new UserDao();
  private PasswordCodec passwordCodec = new PasswordCodec();
  private UserConverter converter = new UserConverter();


  public void register(UserDto userDto)
      throws ExistingUsernameRegistrationException, SimplePasswordRegistrationException {
    if (isUsernameExists(userDto.getUsername())) {
      throw new ExistingUsernameRegistrationException();
    }

    if (isPasswordSimple(userDto.getPassword())) {
      throw new SimplePasswordRegistrationException();
    }

    userDto.setRole(RoleDto.USER);
    userDao.create(converter.toEntity(userDto));


  }

  public UserDto login(String username, String password)
      throws EntityNotFoundException, PasswordMismatchException {
    if (!isUsernameExists(username)) {
      throw new EntityNotFoundException();
    }

    final User user = userDao.findByUsername(username);
    final boolean result = passwordCodec.test(password, user.getHashedPassword());
    if (!result) {
      throw new PasswordMismatchException();
    }

    return converter.toDto(user);
  }

  public Collection<UserDto> findAll() {
    return userDao.findAll().stream()
        .map(entity -> converter.toDto(entity))
        .collect(Collectors.toList());
  }

  private boolean isUsernameExists(String username) {
    try {
      userDao.findByUsername(username);
      return true;
    } catch (EntityNotFoundException e) {
      return false;
    }
  }

  private boolean isPasswordSimple(String password) {
    return password.length() <= 6;
  }
}
