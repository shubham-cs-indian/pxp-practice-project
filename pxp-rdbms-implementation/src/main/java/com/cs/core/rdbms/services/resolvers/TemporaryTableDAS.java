package com.cs.core.rdbms.services.resolvers;

import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.services.CSCache;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * services of temporary table management
 * @author frva
 */
public class TemporaryTableDAS extends RDBMSDataAccessService {
  private AtomicInteger tableNo = new AtomicInteger(1);
  private final String prefix;
  
  public TemporaryTableDAS( RDBMSConnection connection, String prefix) {
    super(connection);
    this.prefix = prefix;
  }

  /**
   * @return a new name for temporary table
   */
  private String getTempTableName( String prefix) {
    return String.format( "%s%03d", prefix, tableNo.getAndIncrement());
  }

  /**
   * @return true when the temporary table already exists
   * @throws RDBMSException
   * @throws SQLException
   */
  private boolean checkTempTable(String tableName) throws SQLException, RDBMSException {
    /*if (CSCache.instance().isKept("temptable:" + tableName))
      return true;*/
    IResultSetParser result = driver.getFunction(currentConnection, 
            RDBMSAbstractFunction.ResultType.INT, "pxp.fn_checkTemporaryTable")
        .setInput(RDBMSAbstractFunction.ParameterType.STRING, tableName)
        .execute();
    boolean tempTableExists = (result.getInt() == 1);
    RDBMSLogger.instance().debug("Check existing temp table %s return %b", tableName, tempTableExists);
    if (tempTableExists)
      CSCache.instance().keep("temptable:" + tableName, "");
    return tempTableExists;
  }

  /**
   * @param tableName the temporary table to be created
   * @param structureQuery the query that returns the structure of the table
   * @throws SQLException
   * @throws RDBMSException 
   */
  private void createEmptyTable( String tableName, String structureQuery) throws SQLException, RDBMSException {
    if ( !checkTempTable( tableName) ) {
      String createSqlCmd = driver.getCreateTemporaryTableExpression( tableName, structureQuery);
      try ( Statement create = currentConnection.getConnection().createStatement() ) {
        create.executeUpdate(createSqlCmd);
      }
    }
  }
  
  /**
   * @param insertQuery
   * @throws SQLException
   * @throws RDBMSException 
   */
  private void insertFromSelect( String tableName, String selectQuery) throws SQLException {
    String insertSqlCmd = String.format( "insert into %s (%s)", tableName, selectQuery);
    Statement insert = currentConnection.getConnection().createStatement();
    insert.executeUpdate(insertSqlCmd);
  }
  
  /**
   * Create a temporary table from a query
   * /!\ the query must include a select and where clauses with no extern parenthesis
   * @param baseQuery the query text statement that fulfill the temporary table with contents
   * @return the temporary table name
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public String createTable( String baseQuery) throws SQLException, RDBMSException {
    String tableName = getTempTableName( prefix);
    createEmptyTable( tableName, baseQuery + " and 1=2");
    insertFromSelect( tableName, baseQuery);
    return tableName;
  }

  /**
   * @param leftResultTable
   * @param rightResultTable
   * @return the temporary table that corresponds to the intersection of two tables
   * @throws SQLException
   * @throws RDBMSException 
   */
  String intersectResults(String leftResultTable, String rightResultTable) throws SQLException, RDBMSException {
    String tableName = getTempTableName( prefix);
    createEmptyTable( tableName, String.format("select * from %s where 1=2", leftResultTable));
    insertFromSelect( tableName, 
        String.format( "select * from %s intersect select * from %s", leftResultTable, rightResultTable));
    return tableName;
  }

  /**
   * @param leftResultTable
   * @param rightResultTable
   * @return the temporary table that corresponds to the union of two tables
   * @throws SQLException
   * @throws RDBMSException 
   */
  String unionResults(String leftResultTable, String rightResultTable) throws SQLException, RDBMSException {
    String tableName = getTempTableName( prefix);
    createEmptyTable( tableName, String.format("select * from %s where 1=2", leftResultTable));
    insertFromSelect( tableName, 
        String.format( "select * from %s union select * from %s", leftResultTable, rightResultTable));
    return tableName;
  }

  /**
   * @param leftResultTable
   * @param rightResultTable
   * @return the temporary table that corresponds to the exclusive union of two tables
   * @throws SQLException
   * @throws RDBMSException 
   */
  String exclusiveResults(String leftResultTable, String rightResultTable) throws SQLException, RDBMSException {
    String tableName = getTempTableName( prefix);
    createEmptyTable( tableName, String.format("select * from %s where 1=2", leftResultTable));
    insertFromSelect( tableName, 
        String.format( 
          "(select * from %s union select * from %s) %s (select * from %s intersect select * from %s)", 
                leftResultTable, rightResultTable, driver.getExcludeOperator(), 
                leftResultTable, rightResultTable));
    return tableName;
  }

  /**
   * @param leftResultTable
   * @param rightResultTable
   * @return the temporary table that corresponds to left results excluding right results
   * @throws SQLException
   * @throws RDBMSException 
   */
  String excludeResults(String leftResultTable, String rightResultTable) throws SQLException, RDBMSException {
    String tableName = getTempTableName( prefix);
    createEmptyTable( tableName, String.format("select * from %s where 1=2", leftResultTable));
    insertFromSelect( tableName, 
        String.format( "select * from %s %s select * from %s", 
                leftResultTable, driver.getExcludeOperator(), rightResultTable));
    return tableName;
  }
  
}
