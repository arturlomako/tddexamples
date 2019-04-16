package com.netcracker.mano.ec6.minsk.tdd.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.netcracker.mano.ec6.minsk.tdd.converters.UserConverter;
import com.netcracker.mano.ec6.minsk.tdd.dao.UserDao;
import com.netcracker.mano.ec6.minsk.tdd.dto.RoleDto;
import com.netcracker.mano.ec6.minsk.tdd.dto.UserDto;
import com.netcracker.mano.ec6.minsk.tdd.entities.User;
import com.netcracker.mano.ec6.minsk.tdd.exceptions.EntityNotFoundException;
import com.netcracker.mano.ec6.minsk.tdd.exceptions.ExistingUsernameRegistrationException;
import com.netcracker.mano.ec6.minsk.tdd.exceptions.PasswordMismatchException;
import com.netcracker.mano.ec6.minsk.tdd.exceptions.SimplePasswordRegistrationException;
import java.util.Collections;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

public class UserServiceTest {

  @Mock
  private UserDao userDao;

  @Spy
  private UserConverter userConverter;

  @InjectMocks
  private UserService userService;

  @Captor
  private ArgumentCaptor<User> captor;

  @Before
  public void setUp() {
    initMocks(this);
  }


  @Test(expected = SimplePasswordRegistrationException.class)
  @SneakyThrows
  public void shouldNotRegisterUserWithSimplePassword() {
    when(userDao.findByUsername("johndue")).thenThrow(new EntityNotFoundException());
    final UserDto user = UserDto.builder()
        .username("johndue")
        .password("1234")
        .build();

    userService.register(user);
  }

  @Test(expected = ExistingUsernameRegistrationException.class)
  @SneakyThrows
  public void shouldNotRegisterUserWithExistsUsername() {
    when(userDao.findByUsername("johndue")).thenReturn(new User());

    final UserDto user = UserDto.builder()
        .username("johndue")
        .password("1234")
        .build();

    userService.register(user);

  }

  @Test
  @SneakyThrows
  public void shouldRegisterUser() {
    when(userDao.findByUsername("johndue")).thenThrow(new EntityNotFoundException());

    final UserDto user = UserDto.builder()
        .username("johndue")
        .password("Crypt0paSS")
        .build();

    userService.register(user);
    verify(userDao, times(1)).create(captor.capture());
    verify(userConverter, times(1)).toEntity(any());

    assertThat(captor.getValue().getHashedPassword(),
        is(equalTo(DigestUtils.md5Hex("Crypt0paSS"))));
    assertThat(captor.getValue().getRole(),
        is(equalTo("USER")));
  }


  @Test(expected = PasswordMismatchException.class)
  @SneakyThrows
  public void shouldNotLoginWithIncorrectPassword() {
    when(userDao.findByUsername("johndue")).thenReturn(
        User.builder()
            .id(1)
            .username("johndue")
            .hashedPassword(DigestUtils.md5Hex("Crypt0paSS"))
            .role("ADMIN")
            .build()
    );

    userService.login("johndue", "root");
  }

  @Test(expected = EntityNotFoundException.class)
  @SneakyThrows
  public void shouldNotLoginWithIncorrectUsername() {
    when(userDao.findByUsername("404")).thenThrow(new EntityNotFoundException());

    userService.login("404", "Crypt0paSS");

  }

  @Test
  @SneakyThrows
  public void shouldLoginWithCorrectCredentials() {
    when(userDao.findByUsername("johndue")).thenReturn(
        User.builder()
            .id(1)
            .username("johndue")
            .hashedPassword(DigestUtils.md5Hex("Crypt0paSS"))
            .role("ADMIN")
            .build()
    );

    final UserDto loginedUser = userService.login("johndue", "Crypt0paSS");
    assertEquals(loginedUser.getId(), 1);
    assertEquals(loginedUser.getRole(), RoleDto.ADMIN);
    assertEquals(loginedUser.getUsername(), "johndue");

    verify(userConverter, times(1)).toDto(any());
  }

  @Test
  public void shouldFindAllUsers() {
    when(userDao.findAll()).thenReturn(Collections.emptyList());
    assertThat(userService.findAll().size(), is(equalTo(0)));
  }

}
