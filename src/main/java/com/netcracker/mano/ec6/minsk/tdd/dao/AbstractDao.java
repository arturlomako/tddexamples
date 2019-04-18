package com.netcracker.mano.ec6.minsk.tdd.dao;

import com.netcracker.mano.ec6.minsk.tdd.configuration.DatabaseProperties;
import com.netcracker.mano.ec6.minsk.tdd.exceptions.GenericDatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class AbstractDao {

  private DatabaseProperties databaseProperties = DatabaseProperties.getInstance();

  AbstractDao() {
  }

  PreparedStatement prepareStatement(String sql) {

    final Connection connection;
    try {
      Class.forName(databaseProperties.getDriver());
      connection = DriverManager
          .getConnection(
              databaseProperties.getUrl(),
              databaseProperties.getUser(),
              databaseProperties.getPassword()
          );
      log.info("Prepared statement to {}: {}", databaseProperties.getUrl(), sql);

      return connection.prepareStatement(sql);
    } catch (SQLException | ClassNotFoundException e) {
      throw new GenericDatabaseException(e);
    }
  }

}
