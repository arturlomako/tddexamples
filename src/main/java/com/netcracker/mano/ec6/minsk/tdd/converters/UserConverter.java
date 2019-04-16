package com.netcracker.mano.ec6.minsk.tdd.converters;

import com.netcracker.mano.ec6.minsk.tdd.dto.RoleDto;
import com.netcracker.mano.ec6.minsk.tdd.dto.UserDto;
import com.netcracker.mano.ec6.minsk.tdd.entities.User;

public class UserConverter {

  private PasswordCodec passwordCodec = new PasswordCodec();

  public UserDto toDto(User user) {
    return UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .role(RoleDto.valueOf(user.getRole()))
        .build();
  }

  public User toEntity(UserDto userDto) {
    return User.builder()
        .id(userDto.getId())
        .username(userDto.getUsername())
        .role(userDto.getRole().toString())
        .hashedPassword(passwordCodec.code(userDto.getPassword()))
        .build();
  }


}
