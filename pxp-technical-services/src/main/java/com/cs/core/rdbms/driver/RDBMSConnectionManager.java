package com.cs.core.rdbms.driver;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * This is the entry point to get a connection that will be handled at monitored
 * as critical resource by the driver. This Manager is a singleton (lazy
 * initialization) that will be indirectly run once per servlet request. Once
 * the servlet request is done, the RDBMSConnectionManager is supposed to be
 * finalized and the allocated connection should be automatically closed at the
 * same time.
 *
 * @author vallee
 */
public final class RDBMSConnectionManager {
  
  private static final int                 SANITY_CHECK      = 100;
  // Singleton implementation
  private static RDBMSConnectionManager    connectionManager = null;
  private final Map<Long, RDBMSConnection> connMap;
  // Singleton instance
  private long                             connSeq           = 0L;  // connection
                                                                    // sequence
                                                                    // for
                                                                    // allocation
  
  /**
   * Default constructor for lazy initialization
   */
  private RDBMSConnectionManager()
  {
    connMap = new ConcurrentHashMap<>();
  }
  
  /**
   * @return the singleton instance.
   */
  public static RDBMSConnectionManager instance()
  {
    if (connectionManager == null) {
      synchronized (RDBMSConnectionManager.class) {
        if (connectionManager == null) {
          connectionManager = new RDBMSConnectionManager();
        }
      }
    }
    return connectionManager;
  }
  
  /**
   * @return the next connection identifier
   */
  private synchronized long connSeqInc()
  {
    if (connSeq > Long.MAX_VALUE - 1) {
      connSeq = 0L;
    }
    return ++connSeq;
  }
  
  /**
   * @return a report with the number of connections per thread
   */
  private String reportThreadConnections() {
    Map<Long,Integer> threadReport = new HashMap<>();
    connMap.keySet().forEach( connID -> {
      
      RDBMSConnection rdbmsConnection = connMap.get(connID);
      if (rdbmsConnection != null) {
        long threadID = rdbmsConnection.getThreadID();
        if (threadReport.containsKey(threadID))
          threadReport.put(threadID, threadReport.get(threadID) + 1);
        else
          threadReport.put(threadID, 1);
      }
      else {
        RDBMSLogger.instance().warn("RDBMS Connection object Not found for connection ID : %d in connection map", connID);
      }
    });

    StringBuilder strReport = new StringBuilder( "Report connection(s) per thread: {");
    threadReport.keySet().forEach( threadID -> { 
      strReport.append( String.format("%04X=>(%d),", threadID, threadReport.get(threadID)));});
    if ( threadReport.size() > 0 )
      strReport.setCharAt( strReport.length()-1, '}');
    return strReport.toString();
  }
  
  /**
   * Acquire a connection and register it as open
   *
   * @return the acquired connection
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public RDBMSConnection acquireConnection() throws RDBMSException
  {
    RDBMSConnection connection;
      RDBMSAbstractDriver driver = (RDBMSAbstractDriver) RDBMSDriverManager.getDriver();
      // RDBMSLogger.instance().debug( "Attempt to acquire connection (used
      // %d)", connMap.size());
      // time to acquire a connection is fixed
      synchronized (this) {
        connection = new RDBMSConnection(driver, connSeqInc(), DataSourceUtils.getConnection(driver.getDataSource()));
        connMap.put(connection.getID(), connection);
      }
      if (connMap.size() != driver.getActivePoolSize()) {
        RDBMSLogger.instance().warn("Acquired connection %d with used/active discrepancy (used %d, active %d, idle %d)", connection.getID(),
            connMap.size(), driver.getActivePoolSize(), driver.getIdlePoolSize());
        RDBMSLogger.instance().info(reportThreadConnections());
      }
      else if (connection.getID() % SANITY_CHECK == 0) { // leave a sanity trace
                                                         // every X connections
        RDBMSLogger.instance().debug("Acquired connection %d (used %d, active %d, idle %d)", connection.getID(), connMap.size(),
            driver.getActivePoolSize(), driver.getIdlePoolSize());
        RDBMSLogger.instance().debug(reportThreadConnections());
      }
      return connection;
  }
  
  /**
   * Release an acquired connection
   *
   * @param connection the connection to be released
   * @throws RDBMSException
   */
  public void releaseConnection(RDBMSConnection connection) throws RDBMSException
  {
    if (!connMap.containsKey(connection.getID())) {
      throw new RDBMSException(1000, "Inconsistent state", "Inconsistent connection release " + connection.getID());
    }
    connection.close();
    connMap.remove(connection.getID());
    if (connection.getID() % SANITY_CHECK == 0) { // leave a sanity trace every
                                                  // X connections
      RDBMSLogger.instance().debug("Released connection %d (used %d)", connection.getID(), connMap.size());
    }
  }
  
  /**
   * Execute a transaction by managing connection acquisition and release
   *
   * @param transaction
   * @throws RDBMSException
   */
  public void runTransaction(ITransaction transaction) throws RDBMSException
  {
    if(!TransactionSynchronizationManager.isActualTransactionActive()) {
      throw new RDBMSException(1000, "Transaction", "Transaction not found");
    }
    RDBMSConnection currentConn = acquireConnection();
    try {
      transaction.execute(currentConn);
    }
    catch (RDBMSException ex) {
      currentConn.release();
      throw ex;
    }
    catch (SQLException ex) {
      currentConn.release();
      throw new RDBMSException(1000, "SQLException: " + ex.getErrorCode(), ex);
    }
    catch (CSFormatException ex) {
      currentConn.release();
      throw new RDBMSException(0, "Format: " + ex.getMessage(), ex);
    }
    catch (Throwable ex) {
      currentConn.release();
      throw ex;
    }
    
    currentConn.release();
  }
  
  /**
   * Finalization of the instance by releasing all registered connections
   *
   * @throws RDBMSException
   * @throws Throwable
   */
  @Override
  public void finalize() throws RDBMSException, Throwable
  {
    try {
      for (long key : connMap.keySet()) {
        connMap.get(key).close();
      }
    }
    finally {
      super.finalize();
    }
  }
  
  /**
   * Release an acquired connection
   *
   * @param connection the connection to be released
   * @throws RDBMSException
   */
  public void releaseConnectionMap(RDBMSConnection connection) throws RDBMSException
  {
    if (!connMap.containsKey(connection.getID())) {
      throw new RDBMSException(1000, "Inconsistent state", "Inconsistent connection release " + connection.getID());
    }
    connMap.remove(connection.getID());
    if (connection.getID() % SANITY_CHECK == 0) { // leave a sanity trace every
                                                  // X connections
      RDBMSLogger.instance().debug("Released connection %d (used %d)", connection.getID(), connMap.size());
      
    }
  }
}
