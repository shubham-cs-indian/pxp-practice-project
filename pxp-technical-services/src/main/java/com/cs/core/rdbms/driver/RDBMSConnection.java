package com.cs.core.rdbms.driver;

import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Encapsulates a connection as provided by the connection pool and controlled by ConnectionManager
 *
 * @author vallee
 */
public class RDBMSConnection {

  private final RDBMSAbstractDriver driver;
  private final long threadID;
  private final Connection currentConn;
  private final long connectionID;
  private final Map<String, Savepoint> savePoint = new HashMap<>();
  private final Stack<String> savePointLIFO = new Stack<>();

  /**
   * Constructor of a new connection by ConnectionManager
   *
   * @param driver current driver of the connection
   * @param id the id of the connection
   * @param connection the allocated connection
   */
  public RDBMSConnection( RDBMSAbstractDriver driver, long id, Connection connection) {
    this.driver = driver;
    threadID = Thread.currentThread().getId();
    connectionID = id;
    currentConn = connection;
  }

  /**
   * @return the ID provided by the connection manager
   */
  protected long getID() {
    return connectionID;
  }
  
  /**
   * @return the Thread ID in which the connection has been opened
   */
  protected long getThreadID() {
    return threadID;
  }

  /**
   * Close the connection when requested by the connection manager
   *
   * @throws RDBMSException
   */
  protected void close() throws RDBMSException {
    try {
      currentConn.close();
    } catch (SQLException ex) {
      throw new RDBMSException(1000, "SQLException:close", ex);
    }
  }

  /**
   * Clear out registered save points
   */
  private void clearSavePoints() {
    savePoint.clear();
    savePointLIFO.clear();
  }

  /**
   * Register save points
   */
  private void registerSavePoint(String name, Savepoint sp) {
    savePoint.put(name, sp);
    savePointLIFO.add(name);
  }

  /**
   * unregister save points that happened until a given save point including that save point
   *
   * @param name the save point name of reference
   */
  private void unregisterSavePointsUntil(String name) {
    while (!savePointLIFO.empty()) {
      String topSavePointName = savePointLIFO.pop();
      savePoint.remove(topSavePointName);
      if (topSavePointName.equals(name)) {
        break;
      }
    }
  }

  /**
   * @return the driver applied in current application
   */
  public RDBMSAbstractDriver getDriver() {
    return driver;
  }
  
  /**
   * @return the current connection of the current object
   */
  public Connection getConnection() {
    return currentConn;
  }

  /**
   * Proceed to fully commit actions executed and release the connection
   *
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void commit() throws RDBMSException {
    try {
      currentConn.commit();
    } catch (SQLException ex) {
      throw new RDBMSException(1200, "SQLException:commit", ex);
    }
    clearSavePoints();
  }

  /**
   * Proceed to fully rollback actions executed and release the connection
   *
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void rollback() throws RDBMSException {
    try {
      currentConn.rollback();
    } catch (SQLException ex) {
      throw new RDBMSException(1200, "SQLException:commit", ex);
    }
    clearSavePoints();
  }

  /**
   * Commit the current statement run in the connection and release it
   *
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void commitAndRelease() throws RDBMSException {
    commit();
    RDBMSConnectionManager.instance().releaseConnection(this);
  }

  /**
   * Rollback the current statement run in the connection and release it
   *
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void rollbackAndRelease() throws RDBMSException {
    try {
      currentConn.rollback();
    } catch (SQLException ex) {
      throw new RDBMSException(1200, "SQLException:commit", ex);
    }
    clearSavePoints();
    RDBMSConnectionManager.instance().releaseConnection(this);
  }

  /**
   * Set a save point in the current transaction (the transaction must be begun)
   *
   * @param savePointName the save point name
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void setSavePoint(String savePointName) throws RDBMSException {
    try {
      Savepoint sp = currentConn.setSavepoint(savePointName);
      registerSavePoint(savePointName, sp);
    } catch (SQLException ex) {
      throw new RDBMSException(2100, "SQLException:save point", ex);
    }
  }

  /**
   * Proceed to fully rollback actions executed and release the connection
   *
   * @param savePointName a save point name already set in the transaction
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void rollbackToSavePoint(String savePointName) throws RDBMSException {
    Savepoint sp = savePoint.get(savePointName);
    if (sp != null) {
      try {
        currentConn.rollback(sp);
      } catch (SQLException ex) {
        throw new RDBMSException(2100, "SQLException:save point", ex);
      }
      unregisterSavePointsUntil(savePointName);
    }
  }

  /**
   * Delegated method to internal connection
   *
   * @param sql is the JDBC call to a function or procedure
   * @return a JDBC Callable Statement
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public CallableStatement prepareCall(String sql) throws RDBMSException {
    try {
      return currentConn.prepareCall(sql);
    } catch (SQLException ex) {
      throw new RDBMSException(3000, "SQLException:prepare", ex);
    }
  }

  /**
   * Delegated method to internal connection
   *
   * @param sql is a SQL command
   * @return a JDBC Prepared Statement
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public PreparedStatement prepareStatement(String sql) throws RDBMSException {
    try {
      return currentConn.prepareStatement(sql);
    } catch (SQLException ex) {
      throw new RDBMSException(3000, "SQLException:prepare", ex);
    }
  }
  
  /**
   * Calls prepareStatement method by using toString()
   * @param sql query as StringBuilder
   * @return
   * @throws RDBMSException
   */
  public PreparedStatement prepareStatement(StringBuilder sql) throws RDBMSException {
    return prepareStatement(sql.toString());
  }

  /**
   * @param array entry collection
   * @return a converted JDBC String ARRAY
   * @throws SQLException 
   */
  public Array newStringArray( Collection<String> array) throws SQLException {
    return driver.getProcedure(this, "").newStringArray(array);
  }

  /**
   * @param array entry collection
   * @return a converted JDBC IID ARRAY
   * @throws SQLException 
   */
  public Array newIIDArray( Collection<Long> array) throws SQLException {
    return driver.getProcedure(this, "").newIIDArray(array);
  }

  /**
   * @param array entry collection
   * @return a converted JDBC integer ARRAY
   * @throws SQLException 
   */
  public Array newIntArray( Collection<Integer> array) throws SQLException {
    return driver.getProcedure(this, "").newIntArray(array);
  }

  /**
   * @param resultSet a result set obtained by query or function call
   * @return the corresponding ResultSet parser
   * @throws java.sql.SQLException
   */
  public IResultSetParser getResultSetParser( ResultSet resultSet) throws SQLException {
    return driver.getResultSetParser( resultSet);
  }
   
  /**
   * @param resultType
   * @param callDeclaration
   * @return the corresponding function handler
   */
  public RDBMSAbstractFunction getFunction( RDBMSAbstractFunction.ResultType resultType,String callDeclaration) {
    return driver.getFunction(this, resultType, callDeclaration);
  }

  /**
   * @param connection
   * @param callDeclaration
   * @return a function handler which is declared to return void
   */
  public RDBMSAbstractFunction getProcedure( String callDeclaration) {
    return driver.getProcedure(this, callDeclaration);
  }
  
  /**
   * Release connection from connection map
   *
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void release() throws RDBMSException {
    RDBMSConnectionManager.instance().releaseConnectionMap(this);
  }

}
