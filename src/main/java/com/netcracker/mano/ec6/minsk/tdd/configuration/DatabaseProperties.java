package com.netcracker.mano.ec6.minsk.tdd.configuration;

import lombok.Getter;

@Getter
public class DatabaseProperties {

  private DatabaseProperties() {}

  private static final DatabaseProperties instance = new DatabaseProperties();

  public static synchronized DatabaseProperties getInstance() {
    return instance;
  }

  private final String driver = "com.postgres.jdbc.Driver";
  private final String url = "jdbc:mysql://localhost/PRODUCTION_DATABASE";
  private final String user = "Passw))))0rd";
  private final String password = ".root";
}
