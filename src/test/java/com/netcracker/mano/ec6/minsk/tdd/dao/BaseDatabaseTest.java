package com.netcracker.mano.ec6.minsk.tdd.dao;

import com.netcracker.mano.ec6.minsk.tdd.configuration.DatabaseProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import lombok.SneakyThrows;
import org.mockito.Mock;
import org.mockito.Mockito;

abstract class BaseDatabaseTest {

  @Mock
  private DatabaseProperties databaseProperties;

  @SneakyThrows
  void cleanDatabase() {
    final Connection connection = DriverManager
        .getConnection(
            databaseProperties.getUrl(),
            databaseProperties.getUser(),
            databaseProperties.getPassword()
        );
    connection.createStatement().executeQuery("truncate table users");
  }

  void mockDatabaseProperties() {
    Mockito.when(databaseProperties.getUrl()).thenReturn("jdbc:mysql://localhost/USERS");
    Mockito.when(databaseProperties.getDriver()).thenReturn("com.mysql.jdbc.Driver");
    Mockito.when(databaseProperties.getUser()).thenReturn("root");
    Mockito.when(databaseProperties.getPassword()).thenReturn(".root");

  }


}
