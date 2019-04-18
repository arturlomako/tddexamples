package com.netcracker.mano.ec6.minsk.tdd.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import com.netcracker.mano.ec6.minsk.tdd.entities.User;
import com.netcracker.mano.ec6.minsk.tdd.exceptions.EntityNotFoundException;
import com.netcracker.mano.ec6.minsk.tdd.exceptions.GenericDatabaseException;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@Ignore
//Используем либо RunWith(MockitoJUnitRunner.class)
//либо initMocks(this);
@RunWith(MockitoJUnitRunner.class)
public class UserDaoTest extends BaseDatabaseTest {

  @InjectMocks
  private UserDao userDao;

  @Before
  public void setUp() {
    super.mockDatabaseProperties();
    super.cleanDatabase();
  }

  @After
  public void tearDown() {
    super.cleanDatabase();
  }

  @Test
  public void shouldCreateUser() {
    userDao.create(buildSimpleUser());

    final int actualSize = userDao.findAll().stream()
        .filter(user -> user.getUsername().equals("johndue"))
        .collect(Collectors.toList())
        .size();

    assertEquals(
        1,
        actualSize
    );
  }

  @Test(expected = GenericDatabaseException.class)
  public void shouldFailOnNonUniqueUsername() {
    userDao.create(buildSimpleUser());
    userDao.create(buildSimpleUser());
  }

  @Test
  public void shouldFindEmptyListIfThereAreNoUsers() {
    assertThat(userDao.findAll().size(), is(equalTo(0)));
  }

  @Test
  public void shouldFindAllUsers() {
    userDao.create(buildSimpleUser("2"));
    userDao.create(buildSimpleUser("1"));
    assertThat(userDao.findAll().size(), is(equalTo(2)));
  }

  @Test
  @SneakyThrows
  public void shouldFindUserByUsername() {
    userDao.create(buildSimpleUser());
    final User user = userDao.findByUsername("johndue");
    assertThat(user.getUsername(), is(equalTo("johndue")));
    assertThat(user.getRole(), is(equalTo("ADMIN")));
    assertThat(user.getHashedPassword(), is(equalTo(String.valueOf("cryptopass".hashCode()))));
  }

  @Test(expected = EntityNotFoundException.class)
  @SneakyThrows
  public void shouldThrowExceptionIfUsernameWasNotFound() {
    userDao.create(buildSimpleUser());
    userDao.findByUsername("404");
  }

  private User buildSimpleUser() {
    return buildSimpleUser("johndue");
  }

  private User buildSimpleUser(String username) {
    return User.builder()
        .username(username)
        .hashedPassword(String.valueOf("cryptopass".hashCode()))
        .role("ADMIN")
        .build();
  }

}
