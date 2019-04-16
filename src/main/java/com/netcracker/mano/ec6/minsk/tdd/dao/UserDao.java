package com.netcracker.mano.ec6.minsk.tdd.dao;

import com.netcracker.mano.ec6.minsk.tdd.entities.User;
import com.netcracker.mano.ec6.minsk.tdd.exceptions.EntityNotFoundException;
import com.netcracker.mano.ec6.minsk.tdd.exceptions.GenericDatabaseException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDao extends AbstractDao {

  private final static String INSERT_USER_SQL = "insert into users (username, password_hash, role) values (?, ?, ?)";
  private final static String SELECT_BY_USERNAME_SQL = "select id, username, password_hash, role from users where username = ?";
  private final static String SELECT_ALL = "select id, username, password_hash, role from users";


  public void create(User newUser) {
    final PreparedStatement preparedStatement = prepareStatement(INSERT_USER_SQL);
    try {
      preparedStatement.setString(1, newUser.getUsername());
      preparedStatement.setString(2, newUser.getHashedPassword());
      preparedStatement.setString(3, newUser.getRole());
      preparedStatement.execute();
    } catch (SQLException e) {
      throw new GenericDatabaseException(e);
    }
  }

  public User findByUsername(String username) throws EntityNotFoundException {
    final PreparedStatement preparedStatement = prepareStatement(SELECT_BY_USERNAME_SQL);
    try {
      preparedStatement.setString(1, username);
      final ResultSet resultSet = preparedStatement.executeQuery();
      if(!resultSet.next()) {
        throw new EntityNotFoundException();
      }
      return mapResultSetToUser(resultSet);
    } catch (SQLException e) {
      throw new GenericDatabaseException(e);
    }
  }

  public Collection<User> findAll() {
    List<User> users = new ArrayList<>();
    final PreparedStatement preparedStatement = prepareStatement(SELECT_ALL);
    try {
      final ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        users.add(mapResultSetToUser(resultSet));
      }

      return users;
    } catch (SQLException e) {
      throw new GenericDatabaseException(e);
    }
  }

  private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
    return User.builder()
        .id(resultSet.getInt("id"))
        .username(resultSet.getString("username"))
        .hashedPassword(resultSet.getString("password_hash"))
        .role(resultSet.getString("role"))
        .build();
  }
}
