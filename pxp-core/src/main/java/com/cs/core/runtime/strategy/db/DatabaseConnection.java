/*package com.cs.core.runtime.strategy.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

public class DatabaseConnection {

  private static BasicDataSource         bDataSource           = null;
  private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<Connection>();

  static {
    if (bDataSource == null) {
      synchronized (DatabaseConnection.class) {
        bDataSource = new BasicDataSource();
        bDataSource.setDriverClassName(PostgreConnectionDetails.DRIVER_CLASS_NAME);
        bDataSource.setUsername(PostgreConnectionDetails.USER_NAME);
        bDataSource.setPassword(PostgreConnectionDetails.PASSWORD);
        bDataSource.setUrl(PostgreConnectionDetails.URL);
        bDataSource.setInitialSize(PostgreConnectionDetails.INITIAL_CONNECTION_SIZE);
        bDataSource.setMaxActive(PostgreConnectionDetails.MAX_ACTIVE_CONNECTION);
        bDataSource.setMinIdle(5);
        bDataSource.setMaxIdle(15);
        bDataSource.setPoolPreparedStatements(true);
        bDataSource.setMaxOpenPreparedStatements(PostgreConnectionDetails.MAX_OPEN_PREPAREED_STATEMENTS);
      }
    }
  }

  public static Connection getConnection() throws SQLException
  {
    Connection connection = connectionThreadLocal.get();
    if (connection == null) {
      connection = bDataSource.getConnection();
      connection.setAutoCommit(false);
      connectionThreadLocal.set(connection);
    }
    return connection;
  }

  public static void closeConnection() throws SQLException
  {
    connectionThreadLocal.get().close();
    connectionThreadLocal.remove();
  }

  public static void commit() throws SQLException
  {
    connectionThreadLocal.get().commit();
  }

  public static void rollBack() throws SQLException
  {
    connectionThreadLocal.get().rollback();
  }
}
*/
