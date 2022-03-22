package com.cs.core.runtime.strategy.usecase.transfer.initialize;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.strategy.db.BasePostgresStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class PostgresConnectionStrategy extends BasePostgresStrategy {
  
  @Bean("postgresConnection")
  public Connection initializePostgresConnection() throws Exception
  {
    Connection connection = null;
    try {
      String connectionPath = "jdbc:postgresql://" + postgresHost + ":" + postgresPort + "/"
          + postgresdatabase;
      Class.forName("org.postgresql.Driver");
      connection = DriverManager.getConnection(connectionPath, postgresuser, postgrespassword);
      RDBMSLogger.instance().info("\n\n********* Postgressql is successfully connected *********\n\n");
    }
    catch (SQLException e) {
      RDBMSLogger.instance().info("Postgressql Connection failure.");
      RDBMSLogger.instance().exception(e);
    }
    return connection;
  }
}
