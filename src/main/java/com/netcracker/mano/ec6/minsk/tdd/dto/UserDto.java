package com.netcracker.mano.ec6.minsk.tdd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  private long id;
  private String username;
  private String password;
  private RoleDto role;
}
