package com.cs.core.rdbms.driver;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.tracking.idao.ILoggerDAO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.ICSEParser;
import com.cs.core.technical.irdbms.IRDBMSDriver;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Common behavior between PGSQL and ORACLE drivers
 *
 * @author vallee
 */
public abstract class RDBMSAbstractDriver implements IRDBMSDriver {

  // Predefined property values
  protected static final String RDBMS_HOME = System.getenv("PXP_RDBMS_HOME");
  
  @Autowired
  protected DataSourceTransactionManager postgresTransactionManager;
  
  @Autowired
  protected BasicDataSource postgresDataSource;

  public static enum DBVendor {
    ORACLE,
    POSTGRES
  }

  /**
   * Initializes the internal dataSouce and starts the event queue engine
   *
   * @param vendor specify "oracle" or "postgres" to fetch the correct driver
   * @throws RDBMSException
   */
  protected void initializeDriver(DBVendor vendor) throws RDBMSException {
    try {
      CSProperties.init(RDBMS_HOME + "/rdbms-props/rdbms.properties");
      RDBMSLogger.instance().info("Starting RDBMS Component on vendor " + vendor);
      
    } catch (CSInitializationException exc) {
      throw new RDBMSException(0, "SYSTEM", exc);
    }
  }

  /**
   * @return the data source initialized by the driver
   */
  public DataSource getDataSource() {
    return postgresTransactionManager.getDataSource();
  }
  
  public DataSource getDataSource_NEW() {
    return postgresTransactionManager.getDataSource();
  }
  
  public DataSourceTransactionManager getTransactionManager() {
    return postgresTransactionManager;
  }

  /**
   * @return the number of active connections
   */
  public int getActivePoolSize() {
    return ((BasicDataSource) getDataSource()).getNumActive();
  }

  /**
   * @return the number of idle connections
   */
  public int getIdlePoolSize() {
    return ((BasicDataSource) getDataSource()).getNumIdle();
  }

  /**
   * @return the Database vendor
   */
  public abstract DBVendor getDBVendor();

  @Override
  public String getDatabaseVendor() {
    return getDBVendor().toString();
  }

  @Override
  public ICSEParser newParser() {
    return new CSEParser();
  }

  @Override
  public ILoggerDAO newLoggerDAO() {
    return RDBMSLogger.instance();
  }

  @Override
  public void shutdown() throws RDBMSException {
    try {
      ((BasicDataSource) getDataSource()).close();
    } catch (SQLException ex) {
      throw new RDBMSException(0, "SQLException:shutdown", ex);
    }
  }

  /**
   * @param connection
   * @param resultType
   * @param callDeclaration
   * @return the corresponding function handler
   */
  public abstract RDBMSAbstractFunction getFunction(
          RDBMSConnection connection,
          RDBMSAbstractFunction.ResultType resultType,
          String callDeclaration);

  /**
   * @param connection
   * @param callDeclaration
   * @return a function handler which is declared to return void
   */
  public RDBMSAbstractFunction getProcedure(RDBMSConnection connection, String callDeclaration) {
    return getFunction(connection, ResultType.VOID, callDeclaration);
  }

  /**
   * @param connection
   * @param callDeclaration
   * @return a function handler which is declared to return a cursor
   */
  public RDBMSAbstractFunction getCursor(RDBMSConnection connection, String callDeclaration) {
    return getFunction(connection, ResultType.CURSOR, callDeclaration);
  }

  /**
   * @param resultSet a result set obtained by query or function call
   * @return the corresponding ResultSet parser
   * @throws java.sql.SQLException
   */
  public abstract IResultSetParser getResultSetParser(ResultSet resultSet) throws SQLException;

  /**
   * @param resultType the type of result expected
   * @param statement the statement obtained just after execution
   * @return the corresponding ResultSet parser
   * @throws java.sql.SQLException
   */
  public abstract IResultSetParser getResultSetParser(
          ResultType resultType, CallableStatement statement) throws SQLException;

  /**
   * @param paramName the name of the aggregated parameter
   * @return the list aggregation expression of the current DB vendor
   */
  public abstract String getListAggregateExpression(String paramName);

  /**
   * @return the database timestamp expression to get system time
   */
  public abstract String getSystemDateExpression();

  /**
   * @param tableName the name of the new temporary table to be created
   * @param query the string query that defines the table structure of the temporary table
   * @return the command expression to be executed according to the DB vendor
   */
  public abstract String getCreateTemporaryTableExpression(String tableName, String query);

  /**
   * @param nbRows the number of rows to offset
   * @return the SQL directive to offset
   */
  public abstract String getOffsetExpression(long nbRows);

  /**
   * @param nbRows the number of rows to offset
   * @param limit the max number of rows to select
   * @return the SQL directive to offset
   */
  public abstract String getOffsetExpression(long nbRows, long limit);

  /**
   * @param colName the column name of the path
   * @param keyElt the element of key that must be found in the path
   * @return the similar where clause implemented by the current vendor
   */
  public abstract String getSimilarPathPattern(String colName, String keyElt);

  public abstract String getExcludeOperator();
}
